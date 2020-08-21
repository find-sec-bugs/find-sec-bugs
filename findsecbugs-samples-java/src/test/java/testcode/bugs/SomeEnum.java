package testcode.bugs;

public enum SomeEnum {

    A("a"),

    B("b"),

    C("c"),

    D("d");

    private final String someName;

    SomeEnum(String someName) {
        this.someName = someName;
    }

    public static SomeEnum fromString(String someName) {
        for (SomeEnum v : SomeEnum.values()) {
            if (v.someName.equals(someName)) {
                return v;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + someName + " found!");
    }

    public String getSomeName() {
        return someName;
    }


    public String toString() {
        return someName;
    }
}
