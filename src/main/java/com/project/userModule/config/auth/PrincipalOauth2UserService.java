package com.project.userModule.config.auth;

import com.project.userModule.user.entity.User;
import com.project.userModule.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public PrincipalOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getClientId();        //제공자
        String providerId = oAuth2User.getAttribute("sub");         //제공자가 주는 id
        String username = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByEmail(email);

        if (userEntity == null) { //db에없다 -> 처음 가입한 유저라면회원가입처리
            userEntity = User.builder()
                    .email(email)
                    .name(username)
                    .role(User.Role.valueOf(role))
                    .status(User.Status.ACTIVE)
                    .build();
            userRepository.save(userEntity);
        }
        System.out.println("여기까진 들어왔지");
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
