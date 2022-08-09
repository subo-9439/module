package com.project.userModule.config;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CertificationNumber {

    public static String create(){
        int leftLimit = 48; //0
        int rightLimit = 122; // z
        int targetStringLength = 6;
        Random random = new Random();
        return  random.ints(leftLimit,rightLimit+1)
                .filter(num -> (num <= 57 || num >= 65) && (num <= 90 || num >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new,StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
