package testcode;

import javax.validation.constraints.Pattern;

public class RedosInPatternAnnotation {
    /**
     * <code>([ ]+)*</code> will trigger a ReDOS.
     */
    @Pattern (regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,8}$")
    private String email;

    public RedosInPatternAnnotation(String e) {
        email = e;
    }

}
