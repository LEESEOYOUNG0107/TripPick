package com.example.trippick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 이 클래스가 스프링의 설정 파일임을 나타냅니다.
@EnableWebSecurity // '웹 보안' 기능을 활성화합니다. (이게 꼭 필요합니다!)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. '출입 명단(보안 필터)'을 설정하는 Bean을 추가합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // "/signup"과 "/login" 경로는 '방문증(로그인)' 없이도 누구나 허용
                        .requestMatchers("/signup", "/login", "/login.css", "/favorites.css", "/images/**").permitAll()
                        // 그 외의 모든 경로는 '방문증(로그인)'이 있어야만 허용
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        // 3. 우리가 직접 만든 로그인 페이지 경로를 알려줌
                        .loginPage("/login")
                        // 4. 로그인 성공 시 이동할 기본 경로 원빈이가 만든 메인페이지로 바꾸기
                        //지금은 임시로 찜페이지로 함
                        .defaultSuccessUrl("/favorites", false)
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