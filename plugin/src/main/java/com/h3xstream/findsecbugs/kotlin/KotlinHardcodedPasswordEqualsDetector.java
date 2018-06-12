package com.h3xstream.findsecbugs.kotlin;

import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.TaintUtil;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.password.IntuitiveHardcodePasswordDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.*;

import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

public class KotlinHardcodedPasswordEqualsDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String KOTLIN_HARD_CODE_PASSWORD_TYPE = "KOTLIN_HARD_CODE_PASSWORD";

    private static final boolean DEBUG = false;

    private static final InvokeMatcherBuilder KOTLIN_INTRINSICS_ARE_EQUALS_METHOD = invokeInstruction() //
            .atClass("kotlin/jvm/internal/Intrinsics").atMethod("areEqual").withArgs("(Ljava/lang/Object;Ljava/lang/Object;)Z");


    public KotlinHardcodedPasswordEqualsDetector(BugReporter bugReporter) {
        super(bugReporter);
        registerVisitor(this);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset) throws DataflowAnalysisException {

        Taint rightValue = fact.getStackValue(offset);
        Taint leftValue = fact.getStackValue(offset == 0 ? 1 : 0);

        boolean passwordVariableLeft = leftValue.isUnknown() && leftValue.hasTag(Taint.Tag.PASSWORD_VARIABLE);
        boolean passwordVariableRight = rightValue.isUnknown() && rightValue.hasTag(Taint.Tag.PASSWORD_VARIABLE);

        //If a constant value is compare with the variable
        //Empty constant are ignored.. it is most likely a validation to make sure it is not empty
        boolean valueHardcodedLeft = TaintUtil.isConstantValueAndNotEmpty(leftValue);
        boolean valueHardcodedRight = TaintUtil.isConstantValueAndNotEmpty(rightValue);

        //Is a constant value that was tag because the value was place in a variable name "password" at some point.
        if ((passwordVariableLeft && valueHardcodedRight) ||
                (passwordVariableRight && valueHardcodedLeft)) {
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }


    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        if (KOTLIN_INTRINSICS_ARE_EQUALS_METHOD.matches(invoke, cpg)) {
            return new InjectionPoint(new int[]{0, 1}, KOTLIN_HARD_CODE_PASSWORD_TYPE);
        }
        return InjectionPoint.NONE;
    }

    @Override
    public void visitInvoke(InvokeInstruction instruction, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) {
        //ByteCode.printOpCode(instruction, cpg);
    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {

    }

    @Override
    public void visitLoad(LoadInstruction instruction, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) {
        //Extract the name of the variable
        int index = instruction.getIndex();
        LocalVariableGen var = StackUtils.getLocalVariable(methodGen, index);
        if (var == null) {
            if (DEBUG) System.out.println("Unable to get field name for index " + index + " in " + methodGen);
            return;
        }
        String fieldName = var.getName();

        boolean isPasswordVariable = false;
        String fieldNameLower = fieldName.toLowerCase();
        for (String password : IntuitiveHardcodePasswordDetector.PASSWORD_WORDS) {
            if (fieldNameLower.contains(password)) {
                isPasswordVariable = true;
            }
        }

        if (!isPasswordVariable) {
            return;
        }

        //Mark local variable
        Taint passwordValue = frameType.getValue(index);
        passwordValue.addTag(Taint.Tag.PASSWORD_VARIABLE);

        if (numProduced <= 0) {
            if (DEBUG) System.out.println("Unexpected number of stack variable produced");
            return;
        }

        //Mark the stack value
        try {
            for (int indexStack = 0; indexStack < numProduced; indexStack++) {
                Taint value = frameType.getStackValue(indexStack);
                value.addTag(Taint.Tag.PASSWORD_VARIABLE);
            }
        } catch (DataflowAnalysisException e) {
        }

    }

    @Override
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taint, int numProduced, ConstantPoolGen cpg) throws Exception {

    }
}
