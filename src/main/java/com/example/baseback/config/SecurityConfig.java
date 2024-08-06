package com.example.baseback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/", "/home", "/base/login").permitAll()  // /base/login 접근 허용
                    .anyRequest().authenticated()  // 다른 모든 요청은 인증이 필요
            )
            .oauth2Login(oauth2 -> oauth2  // OAuth2 로그인 설정
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
            )
            .csrf().disable();  // 필요에 따라 CSRF 보호 비활성화

        return http.build();
    }
}
