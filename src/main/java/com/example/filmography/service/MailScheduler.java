package com.example.filmography.service;

import java.time.LocalDateTime;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MailScheduler {

    private final UserService service;
    private final JavaMailSender javaMailSender;

    public MailScheduler(UserService service, JavaMailSender javaMailSender) {
        this.service = service;
        this.javaMailSender = javaMailSender;
    }

    //https://crontab.guru/
    //http://www.cronmaker.com/?1
//    @Scheduled(cron = "0 0/1 * 1/1 * *") //Каждую минуту
//    public void sendMail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo("voroninden7@gmail.com");
//        message.setSubject("TEST");
//        message.setText("TEST: " + LocalDateTime.now());
//        javaMailSender.send(message);
//        System.out.println("Scheduled task running");
//    }
}
