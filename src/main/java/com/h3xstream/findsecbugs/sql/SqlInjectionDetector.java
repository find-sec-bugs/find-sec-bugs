package com.h3xstream.findsecbugs.sql;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.*;
import edu.umd.cs.findbugs.ba.constant.Constant;
import edu.umd.cs.findbugs.ba.constant.ConstantDataflow;
import edu.umd.cs.findbugs.ba.constant.ConstantFrame;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.Iterator;

/**
 * Class inspired by the detector FindSqlInjection
 */
public abstract class SqlInjectionDetector implements Detector {

    private static final String SQL_INJECTION_TYPE = "SQL_INJECTION";

    private ClassContext classContext;

    private BugReporter bugReporter;

    public SqlInjectionDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        Method[] methodList = javaClass.getMethods();

        for (Method method : methodList) {
            MethodGen methodGen = classContext.getMethodGen(method);
            if (methodGen == null)
                continue;


            try {
                analyzeMethod(classContext, method);
            } catch (DataflowAnalysisException e) {

            } catch (CFGBuilderException e) {

            }
        }
    }

    private void analyzeMethod(ClassContext classContext, Method method) throws DataflowAnalysisException, CFGBuilderException {

        JavaClass javaClass = classContext.getJavaClass();
        this.classContext = classContext;
        MethodGen methodGen = classContext.getMethodGen(method);
        if (methodGen == null)
            return;

        ConstantPoolGen cpg = methodGen.getConstantPool();
        CFG cfg = classContext.getCFG(method);


        ConstantDataflow dataflow = classContext.getConstantDataflow(method);
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();
            Instruction ins = location.getHandle().getInstruction();
            if (!(ins instanceof InvokeInstruction))
                continue;
            InvokeInstruction invoke = (InvokeInstruction) ins;
            if (isAcceptingSqlQuery(invoke, cpg)) {
                ConstantFrame frame = dataflow.getFactAtLocation(location);
                int numArguments = frame.getNumArguments(invoke, cpg);
                Constant value = frame.getStackValue(numArguments - 1);

                if (!value.isConstantString()) {

                    Location prev = getPreviousLocation(cfg, location, true);
                    if (prev == null || !isSafeValue(prev, cpg, cfg)) {

                        bugReporter.reportBug(new BugInstance(this, SQL_INJECTION_TYPE, NORMAL_PRIORITY) //
                                .addClass(javaClass) //
                                .addMethod(javaClass,method) //
                                .addSourceLine(classContext, method, location));
                        //System.out.println("!!!");
                    }
                }
            }
        }

    }

    protected abstract boolean isAcceptingSqlQuery(InvokeInstruction ins, ConstantPoolGen cpg);

    private InstructionHandle getPreviousInstruction(InstructionHandle handle, boolean skipNops) {
        while (handle.getPrev() != null) {
            handle = handle.getPrev();
            Instruction prevIns = handle.getInstruction();
            if (!(prevIns instanceof NOP && skipNops)) {
                return handle;
            }
        }
        return null;
    }

    private Location getPreviousLocation(CFG cfg, Location startLocation, boolean skipNops) {
        Location loc = startLocation;
        InstructionHandle prev = getPreviousInstruction(loc.getHandle(), skipNops);
        if (prev != null)
            return new Location(prev, loc.getBasicBlock());
        BasicBlock block = loc.getBasicBlock();
        while (true) {
            block = cfg.getPredecessorWithEdgeType(block, EdgeTypes.FALL_THROUGH_EDGE);
            if (block == null)
                return null;
            InstructionHandle lastInstruction = block.getLastInstruction();
            if (lastInstruction != null)
                return new Location(lastInstruction, block);
        }
    }

    private boolean isSafeValue(Location location, ConstantPoolGen cpg, CFG cfg) throws CFGBuilderException {
        Instruction prevIns = location.getHandle().getInstruction();
        if (prevIns instanceof LDC || prevIns instanceof GETSTATIC)
            return true;
        if (prevIns instanceof InvokeInstruction) {
            String methodName = ((InvokeInstruction) prevIns).getMethodName(cpg);
            if (methodName.startsWith("to") && methodName.endsWith("String") && methodName.length() > 8)
                return true;
        }
        if (prevIns instanceof AALOAD) {

            Location prev = getPreviousLocation(cfg, location, true);
            if (prev != null) {
                Location prev2 = getPreviousLocation(cfg, prev, true);
                if (prev2 != null && prev2.getHandle().getInstruction() instanceof GETSTATIC) {
                    GETSTATIC getStatic = (GETSTATIC) prev2.getHandle().getInstruction();
                    if (getStatic.getSignature(cpg).equals("[Ljava/lang/String;"))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void report() {
    }
}
