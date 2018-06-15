package javax.servlet.jsp.jstl.core;

import javax.servlet.jsp.tagext.Tag;

public interface LoopTag extends Tag {

    public Object getCurrent();

    public LoopTagStatus getLoopStatus();
}
