package cc.ifinder.novel.config;

import cc.ifinder.novel.security.sign.RepeatedlyReadFilter;
import cc.ifinder.novel.security.sign.ReqInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * by Richard on 2017/8/27
 * desc:请求拦截
 */
@Configuration

class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ReqInterceptor reqInterceptor() {
        return new ReqInterceptor();//自己创建一下这个Spring Bean，这样就能在Spring映射这个拦截器前，把拦截器中的依赖注入给完成了
    }

//    @Bean
//    public JsonReturnHandler jsonReturnHandler(){
//        return new JsonReturnHandler();//初始化json过滤器
//    }
//    @Override
//    public void addReturnValueHandlers(//Json处理和过滤
//            List<HandlerMethodReturnValueHandler> returnValueHandlers) {
//        returnValueHandlers.add(jsonReturnHandler());
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/**");
//        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/pic/**").addPathPatterns("/auth/**");
        registry.addInterceptor(reqInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean repeatedlyReadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
