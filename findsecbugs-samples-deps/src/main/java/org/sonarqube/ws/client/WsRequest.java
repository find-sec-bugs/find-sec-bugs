package org.sonarqube.ws.client;

import java.util.Map;

public interface WsRequest {

    Method getMethod();

    String getPath();

    String getMediaType();

    @Deprecated
    Map<String, String> getParams();

    Parameters getParameters();

    Headers getHeaders();

    enum Method {
        GET, POST
    }
}
