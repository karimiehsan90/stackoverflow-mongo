package com.journaldev.bootifulmongodb.service;

import com.journaldev.bootifulmongodb.conf.Conf;
import com.journaldev.bootifulmongodb.dal.UserRepository;
import com.journaldev.bootifulmongodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private Conf conf;

    public User getByToken(String token){
        return repository.getByUserId(token);
    }

    public void sendEmail(String title, String body, String to){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(conf.getSmtp().getUsername());
        mailSender.setPassword(conf.getSmtp().getPassword());

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(to);
            message.setFrom(conf.getSmtp().getFrom());
            message.setSubject(title);
            message.setBcc(conf.getSmtp().getFrom());
            message.setText(body, true);
        };
        mailSender.send(preparator);
    }
}
