package com.h3xstream.findsecbugs.injection;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;

public interface InjectionSource {

	/**
	 * Before starting intensive analysis on variable flow and iterating on every instruction,
	 * this function will make sure the injection type can occurs in the current class base on
	 * its constant pool gen. All classes dependencies can be found in this pool.
	 * @param cpg
	 * @return
	 */
	boolean isCandidate(ConstantPoolGen cpg);

	/**
	 * The implementation should identify method that are susceptible to injection and return
	 * parameters index that can injected.
	 * @param ins
	 * @param cpg
	 * @return
	 */
	int[] getInjectableParameters( InvokeInstruction ins, ConstantPoolGen cpg );
}
