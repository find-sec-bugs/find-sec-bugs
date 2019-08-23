package testcode.saml;

import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.ParserPool;
import org.springframework.context.annotation.Bean;

public class SafeComments {
    boolean ignoreComments = true;

    @Bean
    ParserPool parserPool1() {
        BasicParserPool pool = new BasicParserPool();
        // DO NOT set ignoreComments = false opens up exploit
        pool.setIgnoreComments(true);
        return pool;
    }


    @Bean
    ParserPool parserPool2() {
        BasicParserPool pool = new BasicParserPool();
        // DO NOT set ignoreComments = false opens up exploit
        pool.setIgnoreComments(ignoreComments);
        return pool;
    }
}
