package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * handles following on topics
 */
@RestController
public class FollowController {

  private TopicService topicService;
  private UserService userService;

  @Autowired
  public FollowController(TopicService topicService,
      UserService userService) {
    this.topicService = topicService;
    this.userService = userService;
  }

  /**
   * Follow a topic and return true if not already following
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/topic/{topicId}/follow")
  public boolean followTopic(@PathVariable("topicId") Long topicId,
      @CurrentUser MyPrincipal principal) {
    Topic topic = topicService.getOne(topicId);
    boolean result = topic.addFollower(userService.getOne(principal.getId()));
    topicService.save(topic);
    return result;
  }

  /**
   * Unfollow a topic and return false if already not following
   */
  @GetMapping("/topic/{topicId}/unfollow")
  public boolean unfollowTopic(@PathVariable("topicId") Long topicId,
      @CurrentUser MyPrincipal principal) {
    Topic topic = topicService.getOne(topicId);
    boolean result = topic.removeFollower(userService.getOne(principal.getId()));
    topicService.save(topic);
    return result;
  }

}
