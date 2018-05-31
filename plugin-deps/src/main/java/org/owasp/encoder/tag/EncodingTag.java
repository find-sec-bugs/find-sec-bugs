package org.owasp.encoder.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class EncodingTag extends SimpleTagSupport {
    
    protected String _value;
    
    public void setValue(String value) {
        this._value = value;
    }

}