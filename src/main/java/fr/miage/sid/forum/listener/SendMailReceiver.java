package fr.miage.sid.forum.listener;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMailReceiver {

  @Autowired
  private MailService mailService;

  /**
   * Sends a notification using JMS queue
   */
  @JmsListener(destination = "mailQueue")
  public void sendMail(Post post) {
    log.info("Mail queue received new message");
    mailService.sendNotifToAllFollowers(post);
  }

}
