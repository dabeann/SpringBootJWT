package security1.security1.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import security1.security1.config.oauth.PrincipalOauth2UserService;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 설정 비활성화
                .csrf(csrf -> csrf.disable())
                // 헤더 설정에서 frameOptions 비활성화
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                // 권한 설정
                .authorizeHttpRequests(authz-> authz
                                .requestMatchers("/user/**").authenticated()
                                //.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                //.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN", "ROLE_USER") 왜오류나냐;
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/mangager/**").hasRole("MANAGER")
                                .anyRequest().permitAll()
                        )
                // 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/")
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        )
                );

        return http.build();
    }
}