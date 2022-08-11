package com.project.userModule.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.userModule.config.auth.PrincipalDetails;
import com.project.userModule.user.entity.User;
import com.project.userModule.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("jwt.secret.key")
    private String key;
    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    //필터체인자체가 하나여야한다 -> 그럼 세팅할 때 미리정해주잖아요
    //
    //필터하나에 대해서 여러개가 상속가능? -> 그럼 빈이 여러개랑 같은 의미인가 빈은그래도 순서 정해놔서 원할 때마다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthorizationFilter.dofilterInternal : 인증이나 권한이 필요한 주소가 요청 됨");

        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {//토큰이 없을시에 또는 잘못된 토큰으로 올 시엔 필터링을 거치지 못함
            chain.doFilter(request,response);
            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC512(key)).build().verify(jwtToken).getClaim("email").asString();
        if (username != null) {
            User userEntity = userRepository.findByEmail(username);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        super.doFilterInternal(request, response, chain);//80프로이상 맞다! -> basic에서 넘어온게 jwt말고도 있을 수도 있기 떄문에
    }
}
