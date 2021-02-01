package testcode.oauth;

import io.vertx.ext.auth.oauth2.OAuth2Options;

import java.util.Properties;

public class VertxOauth2Config {
    private Properties config;

    public void configure(OAuth2Options oAuth2OptionsOptions) {
        oAuth2OptionsOptions
                .setClientID("client")
                .setClientSecret("secret");
    }

    public void configureFalsePositive(OAuth2Options oAuth2OptionsOptions) {
        oAuth2OptionsOptions
                .setClientID("client")
                .setClientSecret(config.getProperty("secretKey"));
    }
}
