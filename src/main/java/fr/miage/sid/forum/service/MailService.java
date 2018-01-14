/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.User;
import java.util.List;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Slf4j
@Service
public class MailService {

  @Autowired
  private JavaMailSender sender;
  @Autowired
  private TemplateEngine templateEngine;

  public void sendEmail() throws Exception {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setTo("gadeau.alexandre@laposte.net");
    helper.setText("How are you?");
    helper.setSubject("Hi");
    sender.send(message);
  }

  public void sendNotificationEmail(User user, User actionner) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom("do-not-reply@spring-mail.com");
      messageHelper.setTo(user.getEmail());
      messageHelper.setSubject("Topic notification");
      String content = build(user.getFirstname(), actionner.getUsername());
      messageHelper.setText(content, true);
    };
    try {
      sender.send(messagePreparator);
    } catch (MailException e) {
      log.error(e.getMessage(), e);
    }
  }

  public void sendNotificationEmail(List<User> users, User actionner) {
    users.forEach((user) -> sendNotificationEmail(user, actionner));
  }


  public String build(String firstname, String responserName) {
    Context context = new Context();
    context.setVariable("firstName", firstname);
    if (responserName != null || !responserName.isEmpty()) {
      context.setVariable("responserName", responserName);
    }
    return templateEngine.process("topic/mailNotification", context);
  }
}
