package org.springframework.web.servlet.config.annotation;

public class CorsRegistration {
    public CorsRegistration allowCredentials(boolean allowCredentials) {return this;}
    public CorsRegistration allowedHeaders(String... headers) {return this;}
    public CorsRegistration allowedMethods(String... methods) {return this;}
    public CorsRegistration allowedOrigins(String... origins) {return this;}
    public CorsRegistration exposedHeaders(String... headers) {return this;}
    public CorsRegistration	maxAge(long maxAge) {return this;};
}
