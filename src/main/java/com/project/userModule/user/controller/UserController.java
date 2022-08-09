package com.project.userModule.user.controller;

import com.project.userModule.gmail.EmailSenderService;
import com.project.userModule.sms.SmsService;
import com.project.userModule.user.dto.AuthDto;
import com.project.userModule.user.dto.UserRequestDto;
import com.project.userModule.user.dto.UserResponseDto;
import com.project.userModule.user.entity.Users;
import com.project.userModule.user.mapper.UserMapper;
import com.project.userModule.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/users")
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
        Users userEntity = mapper.UserRequestDtoToUserEntity(requestBody);
        //2. 회원가입 진행
        Users user = userService.joinMember(userEntity);
        //3 이름과 201 코드 보내기 보내기
        return new ResponseEntity<>(new UserResponseDto<>(user.getName()),HttpStatus.CREATED);
    }

}
