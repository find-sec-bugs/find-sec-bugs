package org.apache.struts2.util;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class StrutsUtil {

    public StrutsUtil(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
    }

    public Object bean(Object aName) throws Exception {
        return null;
    }

    public boolean isTrue(String expression) {
        return false;
    }

    public Object findString(String name) {
        return null;
    }

    public String include(Object aName) throws Exception {
        return null;
    }

    public String urlEncode(String s) {
        return null;
    }

    public String buildUrl(String url) {
        return null;
    }

    public Object findValue(String expression, String className) throws ClassNotFoundException {
        return null;
    }

    public String getText(String text) {
        return null;
    }

    public String getContext() {
        return null;
    }

    public String translateVariables(String expression) {
        return null;
    }

    public List makeSelectList(String selectedList, String list, String listKey, String listValue) {
        return null;
    }

    public int toInt(long aLong) {
        return 0;
    }

    public long toLong(int anInt) {
        return 0;
    }

    public long toLong(String aLong) {
        return 0;
    }

    public String toString(long aLong) {
        return null;
    }

    public String toString(int anInt) {
        return null;
    }

    public String toStringSafe(Object obj) {
        return null;
    }

    static class ResponseWrapper extends HttpServletResponseWrapper {

        ResponseWrapper(HttpServletResponse aResponse) {
            super(aResponse);
        }

        public String getData() {
            return null;
        }

        public ServletOutputStream getOutputStream() {
            return null;
        }

        public PrintWriter getWriter() throws IOException {
            return null;
        }
    }

    static class ServletOutputStreamWrapper extends ServletOutputStream {
        StringWriter writer;

        ServletOutputStreamWrapper(StringWriter aWriter) {
            writer = aWriter;
        }

        public void write(int aByte) {
            writer.write(aByte);
        }
    }
}
