package jwtjwt.jwtTutorial.controller;

import jwtjwt.jwtTutorial.dto.LoginDto;
import jwtjwt.jwtTutorial.dto.TokenDto;
import jwtjwt.jwtTutorial.jwt.JwtFilter;
import jwtjwt.jwtTutorial.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    // 권한 검증

    /**
     * 1. TokenProvider, AuthenticationManagerBuilder를 주입받는다.
     * 2. LoginDto를 이용해 username과 password를 받고 UsernamePasswordAuthenticationToken을 생성한다.
     * 3. authenticationToken을 이용해서 authentication 객체를 생성하기 위해 authenticate 메서드가 실행될 때
     *     - CustomUserDetailsService에서 구현한 loadUserByUsername메서드가 실행되고 최종적으로 Authentication 객체가 생성된다.
     * 4. 생성된 Authentication 객체를 SecurityContext에 저장하고, Authentication 객체를 createToken 메서드를 통해 JWT Token을 생성한다.
     * 5. 생성된 Jwt 토큰을 Response Header와 TokenDto(jwt)를 이용해 ResponseBody에도 넣어 반환한다.
     */

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // LoginDto를 이용해 username과 password를 받고 UsernamePasswordAuthenticationToken을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // authenticationToken을 이용해서 authentication 객체를 생성하기 위해 authenticate 메서드가 실행될 때,
        // CustomUserDetailsService 에서 구현한 loadUserByUsername 메서드가 실행되고 최종적으로 Authentication 객체가 생성된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 생성된 Authentication 객체를 SecurityContext에 저장한다.
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();

        // 생성된 Jwt 토큰을 Response Header에 넣어준다.
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // TokenDto 를 이용해 ResponseBody 에도 넣어 리턴한다.
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}