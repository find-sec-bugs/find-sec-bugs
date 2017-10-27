package org.apache.struts2.dispatcher;

public interface Parameter {

    String getName();

    String getValue();

    boolean isDefined();

    boolean isMultiple();

    String[] getMultipleValues();

    Object getObject();

}
