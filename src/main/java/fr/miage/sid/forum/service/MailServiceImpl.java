package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.domain.User;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

  @Autowired
  private TopicRepository topicRepository;

  @Autowired
  private JavaMailSender sender;

  @Autowired
  private TemplateEngine templateEngine;

  @Override
  @Transactional(readOnly = true)
  public void sendNotifToAllFollowers(Post post) {
    Topic topic = topicRepository.eagerWithFollowers(post.getTopic().getId());
    User author = post.getCreatedBy();
    Set<User> followers = topic.getFollowers();

    if (followers != null && !followers.isEmpty()) {
      followers.forEach(follower -> sendNotifEmail(follower, author, topic, post));
    }
  }

  /**
   * Methods that send an email to a user, about a new post in a topic
   */
  private void sendNotifEmail(User user, User author, Topic topic,
      Post post) {
    // Don't send email to creator
    if (user.getEmail().equals(author.getEmail())) {
      return;
    }

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom("do-not-reply@spring-forum.com");
      messageHelper.setTo(user.getEmail());
      messageHelper.setSubject("New message in Topic: " + topic.getTitle());
      String content = buildMailTemplate(user.getFirstname(), author.getUsername(),
          post.getContent());
      messageHelper.setText(content, true);
    };

    try {
      sender.send(messagePreparator);
    } catch (MailException e) {
      log.error(e.getMessage(), e);
    }
  }

  private String buildMailTemplate(String firstname, String author, String content) {
    Context context = new Context();
    context.setVariable("firstName", firstname);
    context.setVariable("content", content);
    context.setVariable("author", author);

    return templateEngine.process("mail/notification", context);
  }
}
