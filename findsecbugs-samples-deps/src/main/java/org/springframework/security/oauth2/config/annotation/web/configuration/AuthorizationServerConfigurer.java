package org.springframework.security.oauth2.config.annotation.web.configuration;


import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

public interface AuthorizationServerConfigurer {
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception;
}
