package com.example.trippick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 이 클래스가 스프링의 설정 파일임을 나타냄
@EnableWebSecurity // '웹 보안' 기능을 활성화
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. '출입 명단(보안 필터)'을 설정하는 Bean을 추가합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // "/signup"과 "/login" 경로는 '방문증(로그인)' 없이도 누구나 허용
                        .requestMatchers("/","/index.css", "/signup", "/login", "/login.css", "/favorites.css", "/images/**", "/askAI").permitAll()
                        // 그 외의 모든 경로는 '방문증(로그인)'이 있어야만 허용
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        // 3. 우리가 직접 만든 로그인 페이지 경로를 알려줌
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 경로
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동할 경로
                        .permitAll()
                );

        return http.build();
    }
}