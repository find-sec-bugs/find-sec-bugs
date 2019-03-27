package testcode.struts1;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

public class FormWithValidation extends ValidatorForm {

    private static Logger log = Logger.getLogger(FormWithValidation.class.getName());

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The idea is to do minimal validation on inputs.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        //
        boolean validName = false, validEmail = false;
        try {
            validName = ESAPI.validator().isValidInput("TestForm_name", name, "name", 20, false);
            validEmail = ESAPI.validator().isValidInput("TestForm_email", email, "email", 45, false);
        } catch (IntrusionException e) {
            log.severe(e.getMessage());
        }
        if (!validName) errors.add("name", new ActionMessage("TestForm.name.invalid"));
        if (!validEmail) errors.add("email", new ActionMessage("TestForm.email.invalid"));

        return errors;
    }

}
