package jwtjwt.jwtTutorial.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * JwtSecurityConfig class: SecurityConfig에 TokenProvider와 JwtFilter를 적용하기 위한 클래스
     * 1. SecurityConfigurerAdapter를 extends 받아 configure() override 함
     * 2. TokenProvider를 주입받는다.
     * 3. configure()에 new JwtFilter(tokenProvider)를 통해 Security 로직에 해당 필터를 등록
     */
    private final TokenProvider tokenProvider;

    // TokenProvider 생성자 주입
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {

        // JwtFilter를 이용해 Spring Security 필터에 등록
        http.addFilterBefore(
                new JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}