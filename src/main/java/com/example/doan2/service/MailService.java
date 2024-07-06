package com.example.doan2.service;

import com.example.doan2.entity.MailStructer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(DiepCute)")
    private String fromMail;

    public void senMail(String mail, String mailStructer){
        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setText(mailStructer);
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }
}
