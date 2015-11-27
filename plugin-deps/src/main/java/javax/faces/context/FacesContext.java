package javax.faces.context;

import javax.el.ELContext;
import javax.faces.application.Application;

public abstract class FacesContext {
    public static FacesContext getCurrentInstance() {
        return null;
    }

    public abstract Application getApplication();

    public ELContext getELContext() {
        return null;
    }
}
