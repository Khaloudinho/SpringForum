package fr.miage.sid.forum.exception;

public class TopicNotFoundException extends RuntimeException {

  public TopicNotFoundException(String message) {
    super(message);
  }
}
