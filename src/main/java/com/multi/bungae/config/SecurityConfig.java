package com.multi.bungae.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer assetCustomizer() {
        return (web -> web.ignoring().requestMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**"));
    }

    @Bean
    public SecurityFilterChain baseChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 누구나 로그인, 로그아웃, 회원가입, 접근제한, 리소스 api요청 가능
                .requestMatchers("/login", "/logout", "/register", "/access_denied", "/resources/**").permitAll()
                // "/" 도메인 접근 허용
                .requestMatchers("/").authenticated()
                .and()
                .formLogin()
                .loginPage("/login") // 로그인 url 설정
                .loginProcessingUrl("/login_proc") // 로그인 처리 로직 url 설정
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 url 설정
                .failureUrl("/access_denied") // 로그인 실패시 리다이렉트 url 설정 // 인증에 실패했을 때 보여주는 화면 url, 로그인 form으로 파라미터값 error=true로 보낸다.
                .and()
                .csrf().disable();        //로그인 창
        return http.build();
    }
}