package com.ukma.cleaning.utils.security;

import com.ukma.cleaning.utils.exceptions.CantRefreshTokenException;
import com.ukma.cleaning.utils.exceptions.VerifyRefreshTokenException;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenEntity;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            Optional<Cookie> accessToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .findAny();
            String token = null;
            if (accessToken.isPresent())
                token = accessToken.get().getValue();
            String username = null;
            if (token != null) {
                try {
                    username = jwtService.extractUsername(token);
                } catch (ExpiredJwtException e) {
                    String refreshToken = Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals("refreshToken"))
                            .findAny().get().getValue();
                    try {
                        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByToken(refreshToken).orElseThrow(
                                () -> new CantRefreshTokenException("Can`t find refresh token")
                        );
                        RefreshTokenEntity verify = refreshTokenService.verify(refreshTokenEntity);
                        String newJwtToken = jwtService.generateToken(refreshTokenEntity.getUser().getEmail());
                        token = newJwtToken;
                        String newRefreshToken = refreshTokenService.refreshToken(verify.getToken());
                        Cookie newCookie = new Cookie("accessToken", null);
                        newCookie.setMaxAge(0);
                        response.addCookie(newCookie);
                        newCookie = new Cookie("refreshToken", null);
                        newCookie.setMaxAge(0);
                        response.addCookie(newCookie);
                        accessToken.get().setValue(newJwtToken);
                        Cookie newAccess = new Cookie("accessToken", newJwtToken);
                        response.addCookie(newAccess); // maybe don`t work, need the same as new jwt
                        Cookie newRefresh = new Cookie("refreshToken", newRefreshToken);
                        response.addCookie(newRefresh);
                        username = refreshTokenEntity.getUser().getUsername();
                    } catch (VerifyRefreshTokenException | CantRefreshTokenException ex) {
                        Cookie newCookie = new Cookie("accessToken", null);
                        newCookie.setMaxAge(0);
                        response.addCookie(newCookie);
                        newCookie = new Cookie("refreshToken", null);
                        newCookie.setMaxAge(0);
                        response.addCookie(newCookie);
                        response.sendRedirect("/login");
                        return;
                    }


//                log.info("Caught expired JWT token");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                StringBuilder sb = new StringBuilder();
//                sb.append("{ ");
//                sb.append("\"error\": \"Unauthorized\",\n");
//                sb.append("\"message\": \"Token expired\",\n");
//                sb.append("\"path\": \"")
//                        .append(request.getRequestURL())
//                        .append("\",\n");
//                sb.append("} ");
//                response.getWriter().write(sb.toString());
//                return;
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}


//public class JwtAuthFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//    private final RefreshTokenService refreshTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Optional<Cookie> accessToken = Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("accessToken"))
//                .findAny();
//        String token = null;
//        if (accessToken.isPresent())
//            token = accessToken.get().getValue();
//        String username = null;
//        if (token != null) {
//            try {
//                username = jwtService.extractUsername(token);
//            } catch (ExpiredJwtException e) {
//                if (!request.getRequestURI().contains("/api")) {
//                    Cookie deleteTokenCookie = new Cookie("accessToken", null);
//                    deleteTokenCookie.setMaxAge(0);
//                    response.addCookie(deleteTokenCookie);
//                    response.sendRedirect("/login");
//                    return;
//                }
//                log.info("Caught expired JWT token");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                StringBuilder sb = new StringBuilder();
//                sb.append("{ ");
//                sb.append("\"error\": \"Unauthorized\",\n");
//                sb.append("\"message\": \"Token expired\",\n");
//                sb.append("\"path\": \"")
//                        .append(request.getRequestURL())
//                        .append("\",\n");
//                sb.append("} ");
//                response.sendRedirect("/login");
//                return;
//            }
//        }
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (jwtService.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
