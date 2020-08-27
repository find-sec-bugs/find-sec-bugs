package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class CloseableHttpClient {
    public abstract HttpResponse execute(HttpUriRequest httpget);
}
