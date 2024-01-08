package jwtjwt.jwtTutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity   // 1. 웹 보안을 활성화 해주는 어노테이션 추가
public class SecurityConfig {

    // 2. h2-console 과 favicon에 접근하는 건 security에 걸러지지않도록 설정
    @Bean
    public WebSecurityCustomizer configure() {

        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests((registry) ->
                        registry.requestMatchers(
                                        new AntPathRequestMatcher("/api/hello"),   // 2. 입력된 url 만 접근 가능하도록 추가
                                        new AntPathRequestMatcher("/api/authenticate"),
                                        new AntPathRequestMatcher("/api/signup")
                                )
                                .permitAll()
                                .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }
}