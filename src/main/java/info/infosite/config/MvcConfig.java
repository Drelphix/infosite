package info.infosite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CacheControl cacheControl = CacheControl.noCache();
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/").setCacheControl(cacheControl);
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/").setCacheControl(cacheControl);
        registry.addResourceHandler("/pics/**").addResourceLocations("file:/C://pics/").setCacheControl(cacheControl);
    }
}