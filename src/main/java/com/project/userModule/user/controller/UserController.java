package com.project.userModule.user.controller;

import com.project.userModule.config.auth.PrincipalDetails;
import com.project.userModule.config.gmail.EmailSenderService;
import com.project.userModule.sms.SmsService;
import com.project.userModule.user.dto.AuthDto;
import com.project.userModule.user.dto.UserRequestDto;
import com.project.userModule.user.dto.UserResponseDto;
import com.project.userModule.user.entity.User;
import com.project.userModule.user.mapper.UserMapper;
import com.project.userModule.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final UserMapper mapper;
    private final EmailSenderService emailSenderService;

    public UserController(UserService userService, SmsService smsService, UserMapper mapper, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.smsService = smsService;
        this.mapper = mapper;
        this.emailSenderService = emailSenderService;
    }

    //ajax로
    @GetMapping("/phone/{phone}/auth")
    public ResponseEntity phoneAuth(@PathVariable String phone) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String certificationNumber = smsService.certificateAndSend(phone);
        return new ResponseEntity<>(new UserResponseDto<>(new AuthDto("핸드폰으로 인증이 보내졌습니다.",certificationNumber)),HttpStatus.OK);
    }

    //ajax로
    @GetMapping("/email/{email}/auth")
    public ResponseEntity emailAuth(@PathVariable String email) throws InterruptedException {
        String certificationNumber = emailSenderService.verifyEmail(new String[]{email});
        return new ResponseEntity<>(new UserResponseDto<>(new AuthDto("메일이 보내졌습니다.", certificationNumber)), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity joinMember(@RequestBody UserRequestDto.PostJoin requestBody){
        //1. 유저 엔티티로 변환하기
        User userEntity = mapper.UserRequestDtoToUserEntity(requestBody);
        //2. 회원가입 진행
        User user = userService.joinMember(userEntity);
        //3 이름과 201 코드 보내기 보내기
        return new ResponseEntity<>(new UserResponseDto<>(user.getName()),HttpStatus.CREATED);
    }


    //localhost:9090/oauth2/authorization/google

    @GetMapping("/login")
    public @ResponseBody String user(HttpServletRequest httpServletRequest,
                                     @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("####################");
        System.out.println("####################");
        System.out.println("####################");
        System.out.println("####################");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        System.out.println("####################");
        return "user";
    }

    @GetMapping("/loginTest")
    public @ResponseBody String loginTest(Authentication authentication) {
        System.out.println("============/loginTest===========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());
        return "세션 정보 확인";

    }
    // 추가
    @GetMapping("/loginTest2")
    public @ResponseBody String loginTest2(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("============/loginTest2===========");
        System.out.println("userDetails : " + principalDetails.getUser());
        return "세션 정보 확인2";
    }
    @GetMapping("/loginTest3")
    public @ResponseBody String oauthTest(Authentication authentication, @AuthenticationPrincipal OAuth2User user){
        log.info("oAuth2 test 핸들러로 들어왔습니다.");
        OAuth2User authUser = (OAuth2User) authentication.getPrincipal();
        log.info("authentcication : " + authUser.getAttributes());
        log.info("oauth2User :" + user.getAttributes());
        return "테스트확인";
    }
//    @GetMapping("/user/info")
//    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
//        System.out.println(principalDetails.getUser());
//        return "ㅎㅇ";
//    }

}
