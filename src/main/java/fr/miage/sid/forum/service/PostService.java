package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.exception.PermissionPostException;

public interface PostService {
  Post save(Post post);
  Post save(Post post, Long topicId, Long userId) throws PermissionPostException;
}
