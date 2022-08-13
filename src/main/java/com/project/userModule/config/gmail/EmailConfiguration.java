package com.project.userModule.config.gmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    //스프링을 붙일 필요가없다..?
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean enable;

    @Primary
    @Bean
    public EmailSendable simpleEmailSendable() {
        return new SimpleEmailSendableImpl(javaMailSender());
    }
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        //키와 벨류로 매핑하기 위해 왜필요
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth",auth);
//        properties.put("spring.mail.properties.mail.smtp.auth.timeout",timeout);
        properties.put("mail.smtp.starttls.enable",enable);
        return mailSender;
    }
}
