package testcode.cors;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SpringPermissiveCORSInsecure implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") //vulnerable
                .allowedMethods("GET","POST","PUT", "DELETE")
                .allowCredentials(true).maxAge(3600);
    }
}
