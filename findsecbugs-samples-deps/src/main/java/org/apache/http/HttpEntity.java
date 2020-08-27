package org.apache.http;

import java.io.OutputStream;


public interface HttpEntity {
    void writeTo(OutputStream out);
}
