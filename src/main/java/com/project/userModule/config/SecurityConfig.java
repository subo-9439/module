package com.project.userModule.config;

import com.project.userModule.config.auth.PrincipalOauth2UserService;
import com.project.userModule.config.filter.JwtAuthenticationFilter;
import com.project.userModule.config.filter.JwtAuthorizationFilter;
import com.project.userModule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final PrincipalOauth2UserService principalOauth2UserService;
    //필터체인이 하나기때문에
    //원할때 바꿔넣으면되지만
    //이미 실행상태에서는 바꿀수가없다 .
    //즉 저기 메서드내에 dofilterinternal은 다른방식을 고려햇다고 보기엔 애매하다 jwt 말고도 다른방법 폼
    //폼도 유저네임패스워드 <- 폼로그인이
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();  //csrf 설정끄기
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
//                .addFilter(corsFilter)//CustomDsl로 옮김
                .apply(new CustomDsl())//추가
                .and()
                .oauth2Login()
                .userInfoEndpoint() // 추가
                .userService(principalOauth2UserService);
//                .antMatchers("/").hasAuthority()
        return http.build();
    }
    /*
    이전에는 .addFilter(new JwtAuthenticationFilter(authenticationManager())) 메서드를 통해 쉽게 처리할 수 있었습니다.
    하지만 WebSecurityConfigureAdapter가 deprecated되면서 내부에 클래스를 만들어주거나 별도의 처리가 필요해졌습니다.
    CustomDsl이라는 내부 클래스를 만들어 .addFilter(new JwtAuthenticationFilter(authenticationManager())) 처리를 통해 해당 필터를 적용시킵니다.
     */
    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsFilter)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}
