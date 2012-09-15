package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

import java.util.concurrent.atomic.AtomicInteger;

//TODO: Replace with http://code.google.com/p/saferegex/
public class ReDosDetector extends OpcodeStackDetector {

    private boolean DEBUG = false;

    private static final String REDOS_TYPE = "REDOS";

    private static final char[] OPENING_CHAR = {'(','['};

    private static final char[] CLOSING_CHAR = {')',']'};

    private static final char[] PLUS_CHAR = {'+','*','?'};

    private BugReporter bugReporter;

    public ReDosDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("java/util/regex/Pattern")
                && getNameConstantOperand().equals("compile")
                && getSigConstantOperand().equals("(Ljava/lang/String;)Ljava/util/regex/Pattern;")) {
            OpcodeStack.Item item = stack.getStackItem(0);
            if (!StringTracer.isVariableString(item)) {
                String value = (String) item.getConstant();
                analyseRegexString(value);
            }
        } else if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                && getNameConstantOperand().equals("matches")
                && getSigConstantOperand().equals("(Ljava/lang/String;)Z")) {
            OpcodeStack.Item item = stack.getStackItem(0);
            if (!StringTracer.isVariableString(item)) {
                String value = (String) item.getConstant();
                analyseRegexString(value);
            }
        }

    }

    protected void analyseRegexString(String regexString) {

        AtomicInteger maxDepthReach = new AtomicInteger(0);
        visitRegex(regexString,0,new AtomicInteger(0),maxDepthReach,new AtomicInteger(-1));

        if(DEBUG) System.out.printf("%s => %d %n",regexString,maxDepthReach.get());

        if(maxDepthReach.get() >= 2) {
            bugReporter.reportBug(new BugInstance(this, REDOS_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString(regexString));
        }
    }


    /**
     *
     * @param input Regex to analyse
     * @param startPos Current position of the current depth
     * @param currentDepth Number of encapsulation (ie: (((a)+)+)+  == 3)
     * @param outMaxDepth A group can contains multiple groups. This return the depest one.
     * @param outEndPosition The current recursion will return where the cursor is at.
     */
    private void visitRegex(String input, int startPos, AtomicInteger currentDepth, AtomicInteger outMaxDepth, AtomicInteger outEndPosition) {

        if(DEBUG) System.out.printf("Entering \"%s\" (depth=%d) %n", input.substring(startPos), currentDepth.intValue());



        for (int i = startPos; i < input.length(); i++) {
            if (isChar(input,i,CLOSING_CHAR)) {
                //Closing branch
                outEndPosition.set(i);

//                if(hasRepetitionCharAfterChar(input,i,PLUS_CHAR)) {
                    currentDepth.incrementAndGet();
//                    if(DEBUG) System.out.printf("Current depth was %d %n",currentDepth.intValue());
//                    currentDepth.incrementAndGet();
//                    if(currentDepth.intValue() > outMaxDepth.intValue()) {
//                        outMaxDepth.set(currentDepth.intValue());
//                    }
//                }

                if(DEBUG) System.out.printf("Closing _%s_ (max depth=%d) %n", input.substring(startPos,i), outMaxDepth.intValue());
                return;
            }

            if (isChar(input,i,OPENING_CHAR)) {
                //New branch
                AtomicInteger depthNewBranch = new AtomicInteger(currentDepth.intValue());

                if(DEBUG) System.out.printf("before = %d %n",depthNewBranch.intValue());
                visitRegex(input,i+1,depthNewBranch,outMaxDepth,outEndPosition);
                if(DEBUG) System.out.printf("after = %d %n",depthNewBranch.intValue());
//
//
                if(depthNewBranch.intValue() > outMaxDepth.intValue()) {
                    outMaxDepth.set(currentDepth.intValue());
                }

                //Continue from the last branch end
                i = outEndPosition.get();
            }


        }


        if(currentDepth.intValue() > outMaxDepth.intValue()) {
            outMaxDepth.set(currentDepth.intValue());
        }
    }

    /**
     *
     * @param value
     * @param position
     * @param charToTest
     * @return
     */
    private boolean isChar(String value, int position,char[] charToTest) {
        boolean oneCharFound=false;
        for(char ch : charToTest) {
            if(value.charAt(position) == ch) {
                oneCharFound = true;
                break;
            }
        }
        return oneCharFound && (position == 0 || value.charAt(position-1) != '\\');
    }

    private boolean hasRepetitionCharAfterChar(String value, int position, char[] plusChar) {
        if(position >= value.length() -1) {
            return false;
        }

        for(char ch : plusChar) {
            if(value.charAt(position+1) == ch) {
                return true;
            }
        }
        return false;
    }
}
