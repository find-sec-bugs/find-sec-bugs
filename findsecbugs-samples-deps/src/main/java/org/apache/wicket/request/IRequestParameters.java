package org.apache.wicket.request;

import org.apache.wicket.util.string.StringValue;

import java.util.List;
import java.util.Set;

public interface IRequestParameters {
    Set<String> getParameterNames();
    StringValue getParameterValue(String name);
    List<StringValue> getParameterValues(String name);
}
