package com.project.userModule;

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

    @Test
    void test_1() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        smsService.send("핸드폰 번호입력 ","안녕");
    }

}
