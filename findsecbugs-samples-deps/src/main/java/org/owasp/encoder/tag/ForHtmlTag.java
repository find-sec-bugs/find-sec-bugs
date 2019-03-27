
package org.owasp.encoder.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.owasp.encoder.Encode;

public class ForHtmlTag extends EncodingTag {
    @Override
    public void doTag() throws JspException, IOException {
        Encode.forHtml(getJspContext().getOut(), _value);        
    }
}