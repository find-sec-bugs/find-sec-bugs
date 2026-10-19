package jakarta.faces.context;

import jakarta.el.ELContext;
import jakarta.faces.application.Application;

public abstract class FacesContext {

    public abstract Application getApplication();

    public ELContext getELContext() {
        return null;
    }

    public static FacesContext getCurrentInstance() {
        return null;
    }

}
