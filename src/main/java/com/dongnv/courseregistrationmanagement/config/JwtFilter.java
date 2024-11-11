package com.dongnv.courseregistrationmanagement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Cookie[] cookies = request.getCookies();
//        log.info("Cookies Array " + Arrays.toString(cookies)); // Null on login requests
//
//        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
//
//        if (cookies != null) {
//            Optional<String> cookie = Arrays.stream(cookies)
//                    .map(Cookie::getName)
//                    .filter(name -> name.equals(COOKIENAME))
//                    .findFirst();
//
//            request.setAttribute();
//            cookie.ifPresent(s -> request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(s)));
//        }
//
//        filterChain.doFilter(requestWrapper, response);
    }
}
