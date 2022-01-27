package com.h3xstream.findsecbugs.injection.xml;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect string concatenation that appears to be constructing XML or HTML documents.
 */
public class NewXmlInjectionDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String XML_INJECTION_TYPE = "POTENTIAL_XML_INJECTION";

    private static final String[] STRING_CONCAT_CLASS = new String[] {"java/lang/StringBuilder","java/lang/StringBuffer"};

    private static final InvokeMatcherBuilder STRINGBUILDER_APPEND = invokeInstruction() //
            .atClass(STRING_CONCAT_CLASS).atMethod("append");

    private static final boolean DEBUG = false;

    public NewXmlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);

        for(String variant: STRING_CONCAT_CLASS) {

            addParsedInjectionPoint(variant+".append(Ljava/lang/String;)L"+variant+";",new InjectionPoint(new int[] {0}, XML_INJECTION_TYPE));
        }
        registerVisitor(this);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame taintFrame, int offset) throws DataflowAnalysisException {
        Taint taint0 = taintFrame.getStackValue(0);
        Taint taint1 = taintFrame.getStackValue(1);

        if ((!taint0.isSafe() && !taint0.hasTag(Taint.Tag.XSS_SAFE)) && taint1.hasTag(Taint.Tag.XML_VALUE)) {
            return Priorities.NORMAL_PRIORITY;
        }
        else {
            return Priorities.IGNORE_PRIORITY;
        }
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        if(STRINGBUILDER_APPEND.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0,1}, XML_INJECTION_TYPE);
        }
        return InjectionPoint.NONE;
    }

    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) throws DataflowAnalysisException {

        if(STRINGBUILDER_APPEND.matches(invoke,cpg)) {
//            System.out.println("c="+invoke.getClassName(cpg));
//            System.out.println("m="+invoke.getMethodName(cpg));
//            System.out.println(frameType.toString(methodGen));

            Taint appendedTaint = parameters.get(0);
            String appendedString = appendedTaint.getConstantValue();
            if(appendedString!= null) {

                if(appendedString.contains("<") && appendedString.contains(">")) {
                    appendedTaint.addTag(Taint.Tag.XML_VALUE);
                    frameType.getStackValue(0).addTag(Taint.Tag.XML_VALUE);
                }
            }



        }
    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {

    }

    @Override
    public void visitLoad(LoadInstruction instruction, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) {

    }

    @Override
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taint, int numProduced, ConstantPoolGen cpg) throws Exception {

    }


    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        ConstantPoolGen constantPoolGen = classContext.getConstantPoolGen();
        boolean stringConcat = false;
        boolean hasOpenTagInString = false;
        for (String requiredClass : STRING_CONCAT_CLASS) {
            if (constantPoolGen.lookupUtf8(requiredClass) != -1) {
                stringConcat = true;
                break;
            }
        }
        for(int i=0;i<constantPoolGen.getSize();i++) {
            Constant c = constantPoolGen.getConstant(i);
            if(c instanceof ConstantUtf8) {
                ConstantUtf8 utf8value = (ConstantUtf8) c;
                if(utf8value.getBytes().contains("<")) {
                    hasOpenTagInString = true;
                    break;
                }
            }
        }
        return stringConcat && hasOpenTagInString;
    }
}
