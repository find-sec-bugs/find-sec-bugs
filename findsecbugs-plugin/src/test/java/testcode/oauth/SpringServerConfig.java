package testcode.oauth;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.util.Properties;

@EnableAuthorizationServer
public class SpringServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final String RESOURCE_ID = "conference";

    private Properties config;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client")
                .resourceIds(RESOURCE_ID)
                .authorizedGrantTypes("authorization_code", "implicit")
                .authorities("ROLE_CLIENT")
                .scopes("read", "write")
                .secret("secret");
    }

    public void configureFalsePositive(ClientDetailsServiceConfigurer clients) throws Exception {


        clients.inMemory().withClient("client")
                .resourceIds(RESOURCE_ID)
                .authorizedGrantTypes("authorization_code", "implicit")
                .authorities("ROLE_CLIENT")
                .scopes("read", "write")
                .secret(config.getProperty("secretKey"));
    }
}
