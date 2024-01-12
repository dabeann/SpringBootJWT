package jwtjwt.jwtTutorial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {
    /**
     * SecurityUtil class: 간단한 유틸리티 메서드 구현 클래스
     */

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {}

    /**
     * getCurrentUsername(): SecurityContext의 Authentication 객체를 이용해 username을 반환하는 유틸성 메서드
     * 1. SecurityContextHolder에서 Authentication 객체를 꺼냄
     * 2. authentication - getPrincipal() - getUsername() 순서대로 유저명을 꺼내 반환
     *
     * SecurityContext에 Authentication 객체가 저장되는 시점은 JwtFilter의 doFilter() 메서드에서
     * Request가 들어올 때 Authentication 객체를 저장해서 사용한다.
     */
    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}