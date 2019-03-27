package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import java.beans.PropertyEditor;

public interface EditorAwareTag {

    PropertyEditor getEditor() throws JspException;
}
