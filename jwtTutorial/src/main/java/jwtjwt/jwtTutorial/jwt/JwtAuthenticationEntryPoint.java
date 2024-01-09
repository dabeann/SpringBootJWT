package jwtjwt.jwtTutorial.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * JwtAuthenticationEntryPoint.class: 유효한 자격증명을 제공하지 않고, 접근하려할 때 401 Unauthorized 에러를 반환하는 클래스
     *
     * AuthenticationEntryPoint를 상속받아 commence() 메서드를 override 함.
     * HttpServletResponse 인터페이스의 SC_UNAUTHORIZED(401)를 response.sendError()에 담는다.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}