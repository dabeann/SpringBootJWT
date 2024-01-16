package security1.security1.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import security1.security1.config.oauth.PrincipalOauth2UserService;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity
//@EnableGlobalMethodSecurity // deprecated 됨. EnableMethodSecurity 를 대신 사용하라고 한다.
// securedEnabled => Secured 애노테이션 사용 여부, prePostEnabled => Pre&Post Authorize 어노테이션 사용 여부.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
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
                .csrf(AbstractHttpConfigurer::disable)
                // 헤더 설정에서 frameOptions 비활성화
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                // 권한 설정
                .authorizeHttpRequests(authz-> authz
                                .requestMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                        )
                // 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/loginProc") // loginProc 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행
                        .defaultSuccessUrl("/")
                )
                // OAuth2 로그인 설정
                // 로그인이 완료된 뒤 후처리가 필요
                // 1. 코드받기 (인증)
                // 2. 엑세스토큰 (권한)
                // 3. 사용자 프로필 정보 가져오기
                // 4. 정보를 토대로 회원가입을 자동으로 진행시키기도 함 or 추가 정보 필요할수도
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        )
                );
                // 구글 로그인이 완료되면
                // 코드X, (엑세스 토큰 + 사용자 프로필 정보)

        return http.build();
    }
}