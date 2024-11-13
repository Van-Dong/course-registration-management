package com.dongnv.courseregistrationmanagement.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //    ObjectMapper objectMapper;

    @NonFinal
    @Value("${jwt.cookie-name}")
    String COOKIE_NAME;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        //        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        //
        //        response.setStatus(errorCode.getStatusCode().value());
        //        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
        //                .code(errorCode.getCode())
        //                .message(errorCode.getMessage())
        //                .build();
        //
        //        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        //        response.flushBuffer();

        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
}
