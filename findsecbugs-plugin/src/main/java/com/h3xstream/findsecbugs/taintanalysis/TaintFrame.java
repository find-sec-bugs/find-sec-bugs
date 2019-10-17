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
package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Frame;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;

/**
 * Representation of the dataflow value (fact) modeling taint state of local
 * variables and values on stack, consists of {@link Taint} values
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrame extends Frame<Taint> {

    public TaintFrame(int numLocals) {
        super(numLocals);
    }



    public String toString(MethodGen method) {
        String[] variables = new String[method.getLocalVariables().length];
        LocalVariableGen[] variablesGen = method.getLocalVariables();
        for(int i=0; i<variablesGen.length ;i++) {
            variables[i] = variablesGen[i].getName();
        }
        return toString(variables);
    }

    @Override
    public String toString() {
        return toString(new String[getNumLocals()]);
    }

    /**
     * The toString method are intended for debugging.
     * To see the visual state of TaintFrame in IntelliJ, Right-Click on the variable and select "View Text".
     *
     * @param variableNames List of variables names that will be map to local sloths.
     * @return View of the stack followed by the local variables
     */
    public String toString(String[] variableNames) {
        StringBuilder str = new StringBuilder();
        try {
            str.append("+============================\n");
            if(!FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
                str.append("| /!\\ Warning : The taint debugging is not fully activated.\n");
            }
            str.append("| [[ Stack ]]\n");
            int stackDepth = getStackDepth();
            for (int i = 0; i < stackDepth; i++) {
                Taint taintValue = getStackValue(i);
                str.append(String.format("| %s. %s {%s}%n",
                        i, taintValue.getState().toString(), taintValue.toString()));
            }
            if (stackDepth == 0) {
                str.append("| Empty\n");
            }
            str.append("|============================\n");
            str.append("| [[ Local variables ]]\n");
            int nb = getNumLocals();
            for (int i = 0; i < nb; i++) {
                Taint taintValue = getValue(i);
                str.append("| ").append(variableNames[i]).append(" = ")
                        .append(taintValue == null ? "<not set>" : taintValue.toString())
                        .append("\n");
            }
            str.append("+============================\n");
        } catch (DataflowAnalysisException e) {
            str.append("Oups "+e.getMessage());
        }

        return str.toString();
    }
}
