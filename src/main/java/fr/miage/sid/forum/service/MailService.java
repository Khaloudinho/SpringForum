package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;

public interface MailService {

  void sendNotifToAllFollowers(Post postId);

}
