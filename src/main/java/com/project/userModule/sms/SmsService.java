package com.project.userModule.sms;

import com.project.userModule.config.CertificationNumber;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsService {
    private final CertificationNumber certificationNumber;
    @Value("${sens.serviceId}")
    private String serviceId;
    @Value("${sens.secretKey}")
    private String sKey;
    @Value("${sens.accessKey}")
    private String aKey;
    @Value("${sens.from}")
    private String from;

    private Long time = System.currentTimeMillis();//필수

    public SmsService(CertificationNumber certificationNumber) {
        this.certificationNumber = certificationNumber;
    }

    public SmsResponseDto send(String to, String content) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        List<SmsMessageDto> smsMessageDtos = new ArrayList<>(){{
            add(new SmsMessageDto(to, content));
        }};

        SmsRequestDto requestDto = SmsRequestDto.builder()
                .type("SMS")
                .from(from)
                .content("안녕하세요 김수보입니다")
                .messages(smsMessageDtos)
                .build();

        /*
        Content-Type: application/json; charset=utf-8
        x-ncp-apigw-timestamp: {Timestamp}
        x-ncp-iam-access-key: {Sub Account Access Key}
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", aKey);
        headers.set("x-ncp-apigw-signature-v2",makeSignature( time.toString() ));

        HttpEntity<SmsRequestDto> body = new HttpEntity<>(requestDto, headers);

        RestTemplate restTemplate = new RestTemplate();
        SmsResponseDto sendSmsResponseDto = restTemplate.postForObject(URI.create("https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages"),body,SmsResponseDto.class);
        System.out.println(sendSmsResponseDto);
        return sendSmsResponseDto;
    }



    //시그니처 설정하기
    public String makeSignature(String timestamp) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/"+serviceId+"/messages";
        String accessKey = aKey;			// access key id (from portal or Sub Account)
        String secretKey = sKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public String certificateAndSend(String phone) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String to = phone.strip().replace("-", "");
        String generatedString = CertificationNumber.create();

        send(to,"[김수보의 클라우드 플랫폼] 인증번호 ["+ generatedString + "] 를 입력해주세요.");
        return generatedString;
    }
}
