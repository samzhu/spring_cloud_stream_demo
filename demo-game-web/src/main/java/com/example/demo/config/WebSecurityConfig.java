package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //設置攔截規則
                .antMatchers("/", "/login")   ////1根路徑和/login路徑不攔截
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //開啟默認登錄頁面
                .formLogin()
                //默認登錄頁面, 登錄成功跳轉頁面
                .loginPage("/login").defaultSuccessUrl("/gameWebsocket").permitAll()
                .and()
                //退出登录后的默认url是"/home"
                .logout().logoutSuccessUrl("/home").permitAll();
    }

    //設置用戶
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("111").password("111").roles("USER")
                .and()
                .withUser("222").password("222").roles("USER")
                .and()
                .withUser("333").password("333").roles("USER")
                .and()
                .withUser("444").password("444").roles("USER")
                .and()
                .withUser("555").password("555").roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 設置不攔截規則
        web.ignoring().antMatchers(
                "/css/**",
                "/js/**",
                "/images/**",
                "/fonts/**",
                "/**/favicon.ico"
        );
    }
}
