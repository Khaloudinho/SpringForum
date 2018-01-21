package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/topic/{topicId}/follow")
  public String followTopic(@PathVariable("topicId") Long topicId) {
    Topic topic = topicService.getOne(topicId);
//    topic.addFollower(userService.getOne(userId));
    return "Now following topic";
  }

  @GetMapping("/topic/{topicId}/unfollow")
  public void unfollowTopic(@PathVariable("topicId") Long topicId,
      @RequestParam("user") Long userId) {

    Topic topic = topicService.getOne(topicId);
    topic.removeFollower(userService.getOne(userId));
    topicService.save(topic, topic.getProject().getId());
  }

}
