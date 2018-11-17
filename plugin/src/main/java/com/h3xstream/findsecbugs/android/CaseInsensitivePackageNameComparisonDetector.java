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

package com.h3xstream.findsecbugs.android;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;


public class CaseInsensitivePackageNameComparisonDetector extends OpcodeStackDetector {

	private static final String ANDROID_CASE_INSENSITIVE_PACKAGE_NAME_COMPARISON_TYPE="ANDROID_CASE_INSENSITIVE_PACKAGE_NAME_COMPARISON";
	private BugReporter bugReporter;

	public CaseInsensitivePackageNameComparisonDetector(BugReporter bugReporter){
		this.bugReporter = bugReporter;
	}

	@Override
	public void sawOpcode(int seen){
		//printOpCode(seen);

		if(seen == Const.INVOKEVIRTUAL &&
		       (
			   getNameConstantOperand().equals("getPackageName") &&
			   getNameConstantOperand().equals("equalsIgnoreCase")
		       )) {
		       bugReporter.reportBug(new BugInstance(this,ANDROID_CASE_INSENSITIVE_PACKAGE_NAME_COMPARISON_TYPE,Priorities.NORMAL_PRIORITY) //
			.addClass(this).addMethod(this).addSourceLine(this));
		}

	}

}

