package com.dongnv.courseregistrationmanagement.config;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // Vì cái này nằm ở filter --> ngoài MVC --＞ Không thể trả về trực tiếp View được mà phải qua Dispatcher Servlet
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.TEXT_HTML_VALUE);

        // Forward to 'access-denined.html'
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error/access-denied");
        dispatcher.forward(request, response);
    }
}
