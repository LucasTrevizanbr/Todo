package br.com.todo.infraestructure.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigure(){

        return new WebMvcConfigurer(){

            @Override
            public void addCorsMappings(CorsRegistry corsRegistry){
                corsRegistry.addMapping("/**")
                        .allowedMethods("GET", "POST","PUT","DELETE")
                        .allowedHeaders("*")
                        .allowedOrigins("*");
            }
        } ;
    }
}
