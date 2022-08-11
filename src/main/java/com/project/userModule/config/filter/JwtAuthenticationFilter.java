package com.project.userModule.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userModule.config.auth.PrincipalDetails;
import com.project.userModule.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


// JWT로그인 필터
//로그인 시도 -> 성공하면 jwt 생성
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("jwt.secret.key")
    private String key;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("JwetAuthenticationFilter.attemptAuthentication : 로그인시도");
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(),User.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
            //authenticationManager에 토큰을 넘기면 UserDetailsService가 처리한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter.successfulAuthentication : 로그인이 성공해서 JWT를 생성중입니다.");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(key)
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 10)))//10분짜리
                        .withClaim("id",principalDetails.getUser().getId())
                        .withClaim("email",principalDetails.getUsername())
                        .withClaim("password",principalDetails.getPassword())
                        .sign(Algorithm.HMAC512(key));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
