package testcasesupport;

public abstract class AbstractTestCase extends AbstractTestCaseBase {

    public abstract void bad() throws Throwable;

    public abstract void good() throws Throwable;

    public void runTest(String className)
    {
        IO.writeLine("Starting tests for Class " + className);

        try
        {
            good();

            IO.writeLine("Completed good() for Class " + className);
        }
        catch (Throwable throwableException)
        {
            IO.writeLine("Caught a throwable from good() for Class " + className);

            IO.writeLine("Throwable's message = " + throwableException.getMessage());

            StackTraceElement stackTraceElements[] = throwableException.getStackTrace();

            IO.writeLine("Stack trace below");

            for (StackTraceElement stackTraceElement : stackTraceElements)
            {
                IO.writeLine(stackTraceElement.toString());
            }
        }

        try
        {
            bad();

            IO.writeLine("Completed bad() for Class " + className);
        }
        catch (Throwable throwableException)
        {
            IO.writeLine("Caught a throwable from bad() for Class " + className);

            IO.writeLine("Throwable's message = " + throwableException.getMessage());

            StackTraceElement stackTraceElements[] = throwableException.getStackTrace();

            IO.writeLine("Stack trace below");

            for (StackTraceElement stackTraceElement : stackTraceElements)
            {
                IO.writeLine(stackTraceElement.toString());
            }
        }
    } /* runTest */
}
