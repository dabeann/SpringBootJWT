package jwtjwt.jwtTutorial.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * JwtAccessDeniedHandler.class: 필요한 권한이 없는 경우에 403 Forbidden 에러를 반환하는 클래스
     *
     * 1. AccessDenidedHandler를 implements 받아 handle() 메서드를 @Override 함
     * 2. HttpServletResponse 인터페이스의 SC_FORBIDDEN(403) 에러를 response.sendError()에 담는다.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}