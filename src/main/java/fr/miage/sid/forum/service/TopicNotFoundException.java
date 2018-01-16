package fr.miage.sid.forum.service;

public class TopicNotFoundException extends RuntimeException {

  public TopicNotFoundException(String message) {
    super(message);
  }
}
