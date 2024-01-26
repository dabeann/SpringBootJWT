package jwt.jwtserver.config;

import jwt.jwtserver.config.jwt.JwtAuthenticationFilter;
import jwt.jwtserver.config.jwt.JwtAuthorizationFilter;
import jwt.jwtserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 스프링 시큐리티 필터 동작 이전에 동작!
                //.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class)

                // csrf 설정 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // jwt Bearer 인증 방식을 쓸 거라서!
                // STATELESS방식: session을 사용하지 않겠다!
                .sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // form 태그 만들어서 로그인 하는 거 안하겠다!
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

                //.addFilter(corsFilter); // @CrossOrigin (인증X), 시큐리티 필터에 등록 인증 (O)

        http
                .apply(new MyCustomDsl());

                // 권한 설정
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}
