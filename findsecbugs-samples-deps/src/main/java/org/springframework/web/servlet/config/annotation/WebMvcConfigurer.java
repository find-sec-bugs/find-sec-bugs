package org.springframework.web.servlet.config.annotation;

public interface WebMvcConfigurer {

    void addCorsMappings(CorsRegistry registry);
}
