package com.example2.demo2.config;

import com.example2.demo2.services.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().antMatchers("/","/css/**","/js/**").permitAll();

        http.formLogin()
                .loginPage("/login").permitAll() //  /login login.html
                .usernameParameter("email")   // form name
                .passwordParameter("password") // form name
                .loginProcessingUrl("/auth").permitAll() // form action
                .failureUrl("/login?error")
                .defaultSuccessUrl("/allItems");

        http.logout()
                .logoutUrl("/logout").permitAll() // form action name & post method
                .logoutSuccessUrl("/login");

      //  http.csrf().disable();

      //  super.configure(http);
    }




}
