package testcode.pages;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;

public class TapestryPage {
    @Persist
    protected String username;

    protected String password;

    protected Form loginForm;

    @OnEvent(value = "validate", component = "loginForm")
    void onValidateFromLoginForm() {
        if (!("admin".equals(username) && "god".equals(password))) {
            loginForm.recordError("Invalid user name or password.");
        }
    }
}
