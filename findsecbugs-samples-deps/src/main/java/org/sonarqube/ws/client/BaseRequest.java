package org.sonarqube.ws.client;

import java.util.*;

abstract class BaseRequest<SELF extends BaseRequest> implements WsRequest {

    private final String path;

    private String mediaType = "";

    private final DefaultParameters parameters = new DefaultParameters();
    private final DefaultHeaders headers = new DefaultHeaders();

    BaseRequest(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    public SELF setMediaType(String s) {
        return (SELF) this;
    }

    public SELF setParam(String key, String value) {
        return setSingleValueParam(key, value);
    }

    public SELF setParam(String key, Integer value) {
        return setSingleValueParam(key, value);
    }

    public SELF setParam(String key, Long value) {
        return setSingleValueParam(key, value);
    }

    public SELF setParam(String key, Float value) {
        return setSingleValueParam(key, value);
    }

    public SELF setParam(String key, Boolean value) {
        return setSingleValueParam(key, value);
    }

    private SELF setSingleValueParam(String key, Object value) {
        return (SELF) this;
    }

    public SELF setParam(String key, Collection<? extends Object> values) {
        return (SELF) this;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    public SELF setHeader(String name, String value) {
        return (SELF) this;
    }

    private static class DefaultParameters implements Parameters {
        // preserve insertion order
        private final Map<String, List<String>> keyValues = null;

        public String getValue(String key) {
            return keyValues.containsKey(key) ? keyValues.get(key).get(0) : null;
        }

        public List<String> getValues(String key) {
            return keyValues.get(key);
        }

        public Set<String> getKeys() {
            return keyValues.keySet();
        }

        private DefaultParameters setValue(String key, String value) {
            return this;
        }

        private DefaultParameters setValues(String key, Collection<String> values) {
            return this;
        }
    }

    private static class DefaultHeaders implements Headers {
        private final Map<String, String> keyValues = new HashMap<>();

        @Override
        public Optional<String> getValue(String name) {
            return null;
        }

        private DefaultHeaders setValue(String name, String value) {
            return this;
        }

        @Override
        public Set<String> getNames() {
            return null;
        }
    }
}