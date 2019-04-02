package testcode.bugs;

import java.util.Arrays;

public class OutOfBoundMutableSample {


    public String someMethod(Bean someBean, String label) {
        Preconditions.checkArgument(SomeClass.CONSTANT_VALUE.equals(someBean.getStringValue2()) || SomeClass.CONSTANT_VALUE.equals(someBean.getStringValue2()));
        Preconditions.checkArgument(Validator.isValid(someBean.getStringValue1()));

        String parameters = Arrays.toString(someBean.getStringValue1().split(SomeClass.SEPARATOR)); //BOOM!

        if (SomeClass.CONSTANT_VALUE.equals(someBean.getStringValue2())) {
            return "xxxxxx " + label + " yyyyyyy " + parameters;
        }

        return "xxxxxxxx " + label + " yyyyyy " + parameters;
    }


    static class SomeClass {
        public static String CONSTANT_VALUE = "Hello World";
        public static String SEPARATOR = ",";
    }

    static class Preconditions {
        public static void checkArgument(boolean test) {}
    }

    static class Bean {

        public String getStringValue1() {
            return "";
        }

        public String getStringValue2() {
            return "";
        }
    }

    static class Validator {
        public static boolean isValid(String stuff) {
            return false;
        }
    }
}
