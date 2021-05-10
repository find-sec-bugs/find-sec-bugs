package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintDataflow;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.*;
import edu.umd.cs.findbugs.bcel.BCELUtil;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.xml.sax.EntityResolver;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class EntityResolverUnsafeUsageDetector implements Detector {

    private static final String RESOLVE_ENTITY_METHOD_SIGNATURE =
            "(Ljava/lang/String;Ljava/lang/String;)Lorg/xml/sax/InputSource;";
    private static final String RESOLVE_ENTITY_METHOD_NAME = "resolveEntity";
    private static final String BUG_TYPE = "ENTITY_RESOLVER_UNSAFE_USAGE";
    private static final int BUG_PRIORITY = Priorities.NORMAL_PRIORITY;

    private final BugReporter bugReporter;

    public EntityResolverUnsafeUsageDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        for (Method method : classContext.getJavaClass().getMethods()) {
            analyzeMethod(method, classContext);
        }
    }

    @Override
    public void report() {

    }

    private void analyzeMethod(Method method, ClassContext classContext) {
        if (!areMethodArgumentsSafe(method)) {
            addToReport(method, classContext);
        }

        JavaClass javaClass = classContext.getJavaClass();
        if (isEntityResolver(javaClass) && !javaClass.isFinal()) {
            analyzeEntityResolverMethod(method, classContext);
        }
    }

    private boolean areMethodArgumentsSafe(Method method) {
        return Arrays.stream(method.getArgumentTypes())
                .filter(this::isEntityResolver)
                .map(type -> (ObjectType) type)
                .map(this::toClass)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Class::getModifiers)
                .allMatch(Modifier::isFinal);
    }

    private void analyzeEntityResolverMethod(Method method, ClassContext classContext) {
        Optional<Iterator<Location>> locationIterator = getLocationIterator(method, classContext);
        if (!locationIterator.isPresent()) {
            return;
        }

        while (locationIterator.get().hasNext()) {
            Location location = locationIterator.get().next();
            InstructionHandle instructionHandle = location.getHandle();
            Instruction instruction = instructionHandle.getInstruction();
            if (instruction instanceof InvokeInstruction) {
                analyzeEntityResolverInvokeInstruction(method, classContext, location,
                        (InvokeInstruction) instruction);
            }
        }
    }

    private Optional<Iterator<Location>> getLocationIterator(Method method, ClassContext classContext) {
        try {
            return Optional.of(classContext.getCFG(method))
                    .map(CFG::locationIterator);
        } catch (CFGBuilderException e) {
            AnalysisContext.logError("Cannot get CFG", e);
            return Optional.empty();
        }
    }

    private void analyzeEntityResolverInvokeInstruction(Method method, ClassContext classContext, Location location,
                                                        InvokeInstruction instruction) {
        ConstantPoolGen constantPoolGen = classContext.getConstantPoolGen();

        List<Integer> entityResolverArgumentIndexes = getEntityResolverArgumentIndexes(constantPoolGen, instruction);
        if (!entityResolverArgumentIndexes.isEmpty()) {
            int numberOfArguments = instruction.getArgumentTypes(constantPoolGen).length;
            entityResolverArgumentIndexes.stream()
                    .filter(index -> isArgumentThis(index, numberOfArguments, classContext, method, location))
                    .forEach(index -> addToReport(method, classContext, location));
        }

        if (isInstructionResolveEntityCall(constantPoolGen, instruction)) {
            addToReport(method, classContext, location);
        }
    }

    private List<Integer> getEntityResolverArgumentIndexes(ConstantPoolGen constantPoolGen,
                                                           InvokeInstruction instruction) {
        Type[] argumentTypes = instruction.getArgumentTypes(constantPoolGen);
        return IntStream.range(0, argumentTypes.length)
                .filter(index -> isEntityResolver(argumentTypes[index]))
                .boxed()
                .collect(Collectors.toList());
    }

    private boolean isArgumentThis(int argumentIndex, int numberOfArguments, ClassContext classContext,
                                   Method method, Location location) {
        Optional<Taint> taint = getTaint(argumentIndex, numberOfArguments, classContext, method, location);
        return taint.filter(Taint::isUnknown)
                .map(value -> value.getVariableIndex() == 0)
                .orElse(false);
    }

    private Optional<Taint> getTaint(int argumentIndex, int numberOfArguments, ClassContext classContext, Method method,
                                     Location location) {
        try {
            MethodDescriptor methodDescriptor = BCELUtil.getMethodDescriptor(classContext.getJavaClass(), method);
            TaintDataflow taintDataflow = Global.getAnalysisCache().getMethodAnalysis(TaintDataflow.class,
                    methodDescriptor);
            TaintFrame taintFrame = taintDataflow.getFactAtLocation(location);
            return Optional.of(taintFrame.getStackValue(numberOfArguments - argumentIndex - 1));
        } catch (CheckedAnalysisException e) {
            AnalysisContext.logError("Cannot get taint stack value", e);
            return Optional.empty();
        }
    }

    private boolean isInstructionResolveEntityCall(ConstantPoolGen constantPoolGen, InvokeInstruction instruction) {
        return instruction.getMethodName(constantPoolGen).equals(RESOLVE_ENTITY_METHOD_NAME)
                && instruction.getSignature(constantPoolGen).equals(RESOLVE_ENTITY_METHOD_SIGNATURE);
    }

    private boolean isEntityResolver(Type type) {
        if (!(type instanceof ObjectType)) {
            return false;
        }

        ObjectType objectType = (ObjectType) type;
        ObjectType entityResolverObjectType = ObjectType.getInstance(EntityResolver.class.getName());
        try {
            return objectType.equals(entityResolverObjectType) ||
                    Repository.implementationOf(objectType.getClassName(), entityResolverObjectType.getClassName());
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isEntityResolver(JavaClass javaClass) {
        try {
            return Arrays.stream(javaClass.getAllInterfaces())
                    .map(JavaClass::getClassName)
                    .anyMatch(className -> className.equals(EntityResolver.class.getName()));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Optional<Class<?>> toClass(ObjectType objectType) {
        String className = objectType.getClassName();
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    private void addToReport(Method method, ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        bugReporter.reportBug(new BugInstance(this, BUG_TYPE, BUG_PRIORITY)
                .addClass(javaClass)
                .addMethod(javaClass, method)
                .addSourceLine(classContext, method, getLocationIterator(method, classContext).map(Iterator::next).orElse(null)));
    }

    private void addToReport(Method method, ClassContext classContext, Location location) {
        JavaClass javaClass = classContext.getJavaClass();
        bugReporter.reportBug(new BugInstance(this, BUG_TYPE, BUG_PRIORITY)
                .addClass(javaClass)
                .addMethod(javaClass, method)
                .addSourceLine(classContext, method, location));
    }
}
