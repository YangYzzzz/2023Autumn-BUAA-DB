//package com.example.config;
//
//import com.alibaba.fastjson.JSONObject;
//import com.example.entity.RestBean;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        System.out.println("链式编程!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        return http
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/api/auth/login")
//                .successHandler(this::onAuthenticationSuccess)
//                .failureHandler(this::onAuthenticationFailure)
//                .and()
//                .logout()
//                .logoutUrl("/api/auth/logout")
//                .and()
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(this::onAuthenticationFailure)
//                .and()
//                .build();
//    }
//
//    private void onAuthenticationFailure(HttpServletRequest httpServletRequest,
//                                         HttpServletResponse response, AuthenticationException e) throws IOException {
//        response.setCharacterEncoding("utf-8");
//        System.out.println("登陆失败");
//        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, e.getMessage())));
//    }
//
//    private void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
//                                         HttpServletResponse response, Authentication authentication) throws IOException {
//        System.out.println("登陆成功");
//        response.setCharacterEncoding("utf-8");
//        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登陆成功")));
//    }
//
//}
