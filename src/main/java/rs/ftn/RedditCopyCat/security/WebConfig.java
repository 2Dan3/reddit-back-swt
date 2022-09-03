package rs.ftn.RedditCopyCat.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");/*.allowedOrigins("http://localhost:4200").allowedMethods("*");*/
//        registry.addMapping("*").allowedOrigins("http://localhost:4200");

        System.out.println("\n----------\nAdded CORS origins for localhost:4200\n----------\n");
    }
}