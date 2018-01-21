package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;

public interface MailService {

	/**
	* Send a notification to all followers of the topic of the post
	*/
  void sendNotifToAllFollowers(Post post);
}
