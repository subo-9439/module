package com.project.userModule;

import com.project.userModule.config.gmail.EmailSenderService;
import com.project.userModule.sms.SmsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class TestController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailSenderService senderService;

    @DisplayName("sms테스트")
    @Test
    void test_1() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        smsService.send("핸드폰 번호입력 ","안녕");
    }

    @DisplayName("2. gmail 테스트 ")
    @Test
    void test_2() throws InterruptedException {
        String[] to = new String[]{"kws3363@gmail.com"};
        String subject = "코가놈";
        String message = "메세지가 왔니";
        senderService.sendEmail(to,subject,message);
    }


}
