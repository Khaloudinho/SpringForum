package fr.miage.sid.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ForumApplication {

  public static void main(String[] args) {
    SpringApplication.run(ForumApplication.class, args);
  }
}
