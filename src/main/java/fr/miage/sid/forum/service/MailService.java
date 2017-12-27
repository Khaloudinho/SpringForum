/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.sid.forum.service;

import fr.miage.sid.forum.controller.TopicController;
import fr.miage.sid.forum.domain.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author alex
 */
@Component
@EnableAutoConfiguration 
public class MailService {
    
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private TemplateEngine templateEngine;
   
    public void sendEmail() throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("gadeau.alexandre@laposte.net");
        helper.setText("How are you?");
        helper.setSubject("Hi");
        sender.send(message);
    }
    public void sendNotificationEmail(User u,User actionner){
         MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("sample@dolszewski.com");
            messageHelper.setTo(u.getEmail());
            messageHelper.setSubject("Sample mail subject");
            String content = build(u.getFirstname(),actionner.getUsername());
           messageHelper.setText(content, true);
        };
        try {
            sender.send(messagePreparator);
        } catch (MailException e) {
           Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, e.getMessage(), e);  
        }
    }
    public void sendNotificationEmail(List<User> u,User actionner){
        u.forEach((user) -> {
            sendNotificationEmail(user,actionner);
        });
    }

    
    public String build(String firstname, String responserName) {
        Context context = new Context();
        context.setVariable("firstName",firstname);
        if(responserName!=null || !responserName.isEmpty()){
            context.setVariable("responserName", responserName);
        }
        return templateEngine.process("topic/mailNotification", context);
    }
}