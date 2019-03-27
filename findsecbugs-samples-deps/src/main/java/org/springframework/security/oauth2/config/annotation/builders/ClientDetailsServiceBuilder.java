package org.springframework.security.oauth2.config.annotation.builders;

import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;

public class ClientDetailsServiceBuilder<B extends ClientDetailsServiceBuilder<B>> extends
        SecurityConfigurerAdapter<ClientDetailsService, B> implements SecurityBuilder<ClientDetailsService> {
    public ClientBuilder withClient(String clientId) {
        return null;
    }

    @Override
    public ClientDetailsService build() throws Exception {
        return null;
    }

    public final class ClientBuilder {
        public ClientBuilder resourceIds(String... resourceIds) {
            return this;
        }

        public ClientBuilder secret(String secret) {
            return this;
        }

        public ClientBuilder authorizedGrantTypes(String... authorizedGrantTypes) {
            return this;
        }

        public ClientBuilder authorities(String... authorizedGrantTypes) {
            return this;
        }

        public ClientBuilder scopes(String... scopes) {
            return this;
        }

    }
}
