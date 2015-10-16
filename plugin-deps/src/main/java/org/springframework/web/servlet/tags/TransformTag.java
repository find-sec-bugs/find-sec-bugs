package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.beans.PropertyEditor;
import java.io.IOException;

public class TransformTag extends HtmlEscapingAwareTag {

    @Override
    protected final int doStartTagInternal() throws JspException {
        return SKIP_BODY;
    }
}
