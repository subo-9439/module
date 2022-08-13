package com.project.userModule.config.gmail;

import com.project.userModule.config.CertificationNumber;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final EmailSendable emailSendable;
    private final CertificationNumber certificationNumber;
    public EmailSenderService(JavaMailSender mailSender, EmailSendable emailSendable, CertificationNumber certificationNumber) {
        this.mailSender = mailSender;
        this.emailSendable = emailSendable;
        this.certificationNumber = certificationNumber;
    }

    public void sendEmail(String[] to,String subject, String message) throws MailSendException,InterruptedException{
        emailSendable.send(to,subject,message);
    }

    public String verifyEmail(String[] to) throws InterruptedException {
        String cfNumber = certificationNumber.create();
        String subject = "\uD83D\uDE0A\uD83D\uDE0A\uD83D\uDE0A 김수보의 클라우드 플랫폼 \uD83D\uDE0A\uD83D\uDE02\uD83E\uDD23\uD83E\uDD23\uD83E\uDD23";
        String message = "안녕하세요 김수보의 클라우드 플랫 폼입니다.\n"+
                "고객님의 인증번호는 ["+cfNumber+"] 입니다.";
        emailSendable.send(to,subject,message);
        return cfNumber;
    }

}
