package testcode.cors;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SpringPermissiveCORSSecure implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("safe.com") //specific domain
                .allowedMethods("GET","POST","PUT", "DELETE")
                .allowCredentials(true).maxAge(3600);
    }
}
