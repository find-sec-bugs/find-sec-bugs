package org.springframework.security.config.annotation.web.builders;

import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

public class HttpSecurity {

    public CsrfConfigurer csrf() throws Exception {
        return new CsrfConfigurer();
    }

}
