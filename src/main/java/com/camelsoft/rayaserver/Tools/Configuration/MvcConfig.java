package com.camelsoft.rayaserver.Tools.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        registry.jsp("/src/main/WEB-INF/jsp/", ".jsp");
    }


    public @Override void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200","https://profile.rayafinancing.com","https://development.rayafinancing.com","https://appleid.apple.com","https://admin.rayafinancing.com","http://localhost:4201","https://dashboard.rayafinancing.com","https://server.rayafinancing.com","https://www.rayafinancing.com","https://admin.camel-soft.com","https://client.camel-soft.com","https://agent.camel-soft.com","https://server.camel-soft.com","https://rayafinancing.com","https://rayafinancing.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory("WebContent", registry);
        exposeDirectory("/WebContent/course_videos_file", registry);
        exposeDirectory("/var/lib/jenkins/workspace/server/src/main/resources", registry);
        registry.addResourceHandler("/**").addResourceLocations("");
        registry.addResourceHandler("**/swagger-ui.html/**")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/STATIC/**")
                .addResourceLocations("classpath:/STATIC/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());


    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file://"+ uploadPath + "/").setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    }



}
