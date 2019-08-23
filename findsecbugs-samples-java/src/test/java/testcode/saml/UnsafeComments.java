package testcode.saml;

import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.context.annotation.Bean;

/**
 * Code sample taken from : https://spring.io/blog/2018/03/01/spring-security-saml-and-this-week-s-saml-vulnerability
 */
public class UnsafeComments {

    @Bean
    ParserPool parserPool1() {
        BasicParserPool pool = new BasicParserPool();
        // DO NOT set ignoreComments = false opens up exploit
        pool.setIgnoreComments(false);
        return pool;
    }


    @Bean
    ParserPool parserPool2() {
        StaticBasicParserPool pool = new StaticBasicParserPool();
        // DO NOT set ignoreComments = false opens up exploit
        pool.setIgnoreComments(false);
        return pool;
    }
}
