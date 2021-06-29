package com.example2.demo2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource src=new ReloadableResourceBundleMessageSource();
        src.setBasename("classpath:languages");
        src.setDefaultEncoding("UTF-8");

        return src;
    }

    @Bean
    public CookieLocaleResolver localeResolver(){
        CookieLocaleResolver res=new CookieLocaleResolver();
        res.setDefaultLocale(new Locale("eng"));
        res.setCookieName("language");
        res.setCookieMaxAge(3600*24*365);

        return res;
    }

    @Bean  // peredayet get parameter
    public LocaleChangeInterceptor localeInterceptor(){
        LocaleChangeInterceptor interceptor=new LocaleChangeInterceptor();
        interceptor.setParamName("lg");

        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(localeInterceptor());
    }
}
