/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
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
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect string concatenation that appears to be constructing XML or HTML documents.
 *
 * @author baloghadamsoftware, h3xstream
 */
public class XmlInjectionDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String XML_INJECTION_TYPE = "POTENTIAL_XML_INJECTION";
    private static final String[] STRING_CONCAT_CLASS = new String[] {"java/lang/StringBuilder","java/lang/StringBuffer"};

    private static final InvokeMatcherBuilder STRINGBUILDER_APPEND = invokeInstruction() //
            .atClass(STRING_CONCAT_CLASS).atMethod("append");

    private static final boolean DEBUG = false;

    public XmlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);

        for(String variant: STRING_CONCAT_CLASS) { //Small hack to avoid loading a signature files for two functions.
            addParsedInjectionPoint(variant+".append(Ljava/lang/String;)L"+variant+";",new InjectionPoint(new int[] {0}, XML_INJECTION_TYPE));
        }
        registerVisitor(this);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame taintFrame, int offset) throws DataflowAnalysisException {
        Taint taint0 = taintFrame.getStackValue(0);
        Taint taint1 = taintFrame.getStackValue(1);

        /**
         * If the value argument passed to append is unsafe (not constant)[1] and not sanitize for XML (XSS_SAFE) [2]
         * and the StringBuilder to which the dynamic value is added is within XML tags [3]
         * [1] && [2] && [3]
         */
        if (!taint0.isSafe() && !taint0.hasTag(Taint.Tag.XSS_SAFE) && taint1.hasTag(Taint.Tag.XML_VALUE)) {
            return Priorities.NORMAL_PRIORITY;
        }
        else {
            return Priorities.IGNORE_PRIORITY;
        }
    }

    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, Taint instanceTaint, List<Taint> parameters, ConstantPoolGen cpg, Location location) throws DataflowAnalysisException {

        if(STRINGBUILDER_APPEND.matches(invoke,cpg)) {

            Taint appendedTaint = parameters.get(0);
            String appendedString = appendedTaint.getConstantValue();
            if(appendedString!= null) {

                //When the string with a tag is sent to the append function.
                //The StringBuilder class will be tagged as XML_VALUE
                if(appendedString.contains("<") && appendedString.contains(">")) {
                    appendedTaint.addTag(Taint.Tag.XML_VALUE);
                    frameType.getStackValue(0).addTag(Taint.Tag.XML_VALUE);
                }
            }



        }
    }

    /**
     * Before we added new tag to the taint analysis and add more effort, here is a linear search on the constant pool.
     * Constant pool include all the constant use in the code of the class. It contains class references and string value.
     *
     * If there are no XML in string in the class, we are add not going to run this additional visitor.
     *
     * @param classContext Information about the class that is about to be analyzed
     * @return
     */
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
