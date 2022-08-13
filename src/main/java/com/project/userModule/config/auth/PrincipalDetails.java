package com.project.userModule.config.auth;

import com.project.userModule.user.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
//https://velog.io/@yoho98/SpringSecurity-Authentication%EA%B0%9D%EC%B2%B4%EC%99%80-UserDetailsOAuth2User
//UserDetails와 ,OAuth2User에는 User객체가 없어서 만들어줘야한다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String,Object> attributes;

    //일반로그인(JWT)
    public PrincipalDetails(User user) {
        System.out.println("#######################################");
        System.out.println("#######################################");
        System.out.println("나 잘나옴?");
        System.out.println(user.toString());
        this.user = user;
    }


    //oAuth로그인
    public PrincipalDetails(User user, Map<String,Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() {
        return null;
    }
}
