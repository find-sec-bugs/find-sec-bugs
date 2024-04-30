package testcode;

import jakarta.validation.constraints.Pattern;

public class JakartaRedosInPatternAnnotation {
    /**
     * <code>([ ]+)*</code> will trigger a ReDOS.
     */
    @Pattern (regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,8}$")
    private String email;

    public JakartaRedosInPatternAnnotation(String e) {
        email = e;
    }

}
