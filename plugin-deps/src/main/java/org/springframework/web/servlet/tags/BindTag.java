package org.springframework.web.servlet.tags;

import org.springframework.validation.Errors;

import javax.servlet.jsp.PageContext;
import java.beans.PropertyEditor;

public class BindTag extends HtmlEscapingAwareTag implements EditorAwareTag {

    public void setPath(String path) {
    }


    public String getPath() {
        return null;
    }

    public void setIgnoreNestedPath(boolean ignoreNestedPath) {
    }

    public boolean isIgnoreNestedPath() {
        return false;
    }


    @Override
    protected final int doStartTagInternal() throws Exception {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public final String getProperty() {
        return null;
    }

    /**
     * Retrieve the Errors instance that this tag is currently bound to.
     * Intended for cooperating nesting tags.
     * @return the current Errors instance, or {@code null} if none
     */
    public final Errors getErrors() {
        return null;
    }

    @Override
    public final PropertyEditor getEditor() {
        return null;
    }


    @Override
    public void doFinally() {
    }
}
