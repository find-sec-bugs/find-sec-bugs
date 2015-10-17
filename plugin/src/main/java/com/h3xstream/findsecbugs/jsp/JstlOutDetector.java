package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;

import java.util.Iterator;

public class JstlOutDetector implements Detector {
    private static final String JSP_JSTL_OUT = "JSP_JSTL_OUT";

    private BugReporter bugReporter;

    public JstlOutDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        for (Method m : javaClass.getMethods()) {

            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        //Conditions that needs to fill to identify the vulnerability
        boolean escapeXmlSetToFalse = false;
        Location locationWeakness = null;

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();
            ByteCode.printOpCode(inst, cpg);

//        JspSpringEvalDetector: [0047]  aload   4
//        JspSpringEvalDetector: [0049]  iconst_1
//        JspSpringEvalDetector: [0050]  invokevirtual   org/apache/taglibs/standard/tag/rt/core/OutTag.setEscapeXml (Z)V

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                if ("org.apache.taglibs.standard.tag.rt.core.OutTag".equals(invoke.getClassName(cpg)) &&
                        "setEscapeXml".equals(invoke.getMethodName(cpg)) &&
                        "(Z)V".equals(invoke.getSignature(cpg))) {
                    Integer value = ByteCode.getConstantInt(location.getHandle().getPrev());
                    if (value != null && value == 0) {
                        escapeXmlSetToFalse = true;
                        locationWeakness = location;
                    }
                }
            }
        }

        //Both condition have been found in the same method
        if (escapeXmlSetToFalse) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, JSP_JSTL_OUT, Priorities.NORMAL_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        }
    }

    @Override
    public void report() {
    }

}
