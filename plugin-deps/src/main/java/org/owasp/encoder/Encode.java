package org.owasp.encoder;

import java.io.IOException;
import java.io.Writer;

public final class Encode {
    public static String forHtml(String input) {
        return forXml(input);
    }

    public static void forHtml(Writer out, String input) throws IOException {
        forXml(out, input);
    }

    public static String forHtmlContent(String input) {
        return forXmlContent(input);
    }

    public static void forHtmlContent(Writer out, String input)
            throws IOException
    {
        forXmlContent(out, input);
    }

    public static String forHtmlAttribute(String input) {
        return forXmlAttribute(input);
    }

    public static void forHtmlAttribute(Writer out, String input)
            throws IOException
    {
        forXmlAttribute(out, input);
    }

    public static String forHtmlUnquotedAttribute(String input) {
        return "";
    }

    public static void forHtmlUnquotedAttribute(Writer out, String input)
            throws IOException
    {
        
    }

    public static String forCssString(String input) {
        return "";
    }

    public static void forCssString(Writer out, String input)
            throws IOException
    {
        
    }

    public static String forCssUrl(String input) {
        return "";
    }

    public static void forCssUrl(Writer out, String input)
            throws IOException
    {
        
    }
    
    @Deprecated public static String forUri(String input) {
        return "";
    }

    @Deprecated public static void forUri(Writer out, String input)
            throws IOException
    {
        
    }

    public static String forUriComponent(String input) {
        return "";
    }

    public static void forUriComponent(Writer out, String input)
            throws IOException
    {
    }

    public static String forXml(String input) {
        return "";
    }
    
    public static void forXml(Writer out, String input)
            throws IOException
    {
    }

    public static String forXmlContent(String input) {
        return "";
    }

    public static void forXmlContent(Writer out, String input)
            throws IOException
    {
    }

    public static String forXmlAttribute(String input) {
        return "";
    }

    public static void forXmlAttribute(Writer out, String input)
            throws IOException
    {
    }
    
    public static String forXmlComment(String input) {
        return "";
    }

    public static void forXmlComment(Writer out, String input)
            throws IOException
    {
    }

    public static String forCDATA(String input) {
        return "";
    }

    public static void forCDATA(Writer out, String input)
            throws IOException
    {
    }

    public static String forJava(String input) {
        return "";
    }

    public static void forJava(Writer out, String input)
            throws IOException
    {
    }

    public static String forJavaScript(String input) {
        return "";
    }

    public static void forJavaScript(Writer out, String input)
            throws IOException
    {
    }
    
    public static String forJavaScriptAttribute(String input) {
        return "";
    }
    
    public static void forJavaScriptAttribute(Writer out, String input)
            throws IOException
    {
    }

    public static String forJavaScriptBlock(String input) {
        return "";
    }

    public static void forJavaScriptBlock(Writer out, String input)
            throws IOException
    {
        
    }

    public static String forJavaScriptSource(String input) {
        return "";
    }

    public static void forJavaScriptSource(Writer out, String input)
            throws IOException
    {
    }

//    static String encode(Encoder encoder, String str) {
//        
//    }
//    
//    static void encode(Encoder encoder, Writer out, String str)
//            throws IOException
//    {
//    }

}
