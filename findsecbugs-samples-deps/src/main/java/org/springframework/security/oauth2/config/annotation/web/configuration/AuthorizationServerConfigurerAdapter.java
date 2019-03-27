package org.springframework.security.oauth2.config.annotation.web.configuration;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

public class AuthorizationServerConfigurerAdapter implements AuthorizationServerConfigurer {


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

    }
}
