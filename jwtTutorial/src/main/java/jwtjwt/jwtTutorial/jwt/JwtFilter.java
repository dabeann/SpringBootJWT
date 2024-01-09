package jwtjwt.jwtTutorial.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    /**
     * 1. GenericFilterBean 을 extends 해서 doFilter() 를 override 해준다.
     * 2. 앞서 구현한 TokenProvider 를 주입받는다.
     */

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 필터링 로직은 doFilter() 안에 구현 doFilter() 역할: 토큰의 인증정보 (Authentication)를 SecurityContext에 저장하는 역할 수행 1. ServletRequest
     * 에서 토큰을 받아 온다. 2. 받아온 토큰을 tokenProvider.validateToken(jwt) 로 유효성 검증을 한다. 3. 토큰이 정상이라면
     * tokenProvider.getAuthentication(jwt) 로 토큰에서 Authentication 객체를 받아온다. 4.
     * SecurityContextHolder.getContext().setAuthentication(authentication) 코드와 같이 Security Context에 받아온 Authentication을
     * 저장해준다.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // Request 에서 토큰을 받아옴
        String jwt = resolveToken(httpServletRequest);

        String requestURI = httpServletRequest.getRequestURI();

        // 받아온 jwt 토큰을 validateToken 메서드로 유효성 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            // 토큰이 정상이라면 Authentication 객체를 받아옴
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Request Header에서 토큰 정보를 가져오기 위한 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}