package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.service.MailService;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

  private final PostService postService;
  private final TopicService topicService;
  private final MailService mailService;

  @Autowired
  public PostController(PostService postService,
      TopicService topicService, MailService mailService) {
    this.postService = postService;
    this.topicService = topicService;
    this.mailService = mailService;
  }

  @GetMapping("/topic/{topicId}/post/create")
  @PreAuthorize("isAuthenticated() and hasPermission(#topicId, 'topic' ,T(fr.miage.sid.forum.domain.Permission).WRITE)")
  public ModelAndView getPostCreateForm(Post post, @PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView("post/create");
    modelAndView.addObject(post);
    modelAndView.addObject("topicId", topicId);
    return modelAndView;
  }

  @PostMapping("/topic/{topicId}/post")
  @PreAuthorize("isAuthenticated() and hasPermission(#topicId, 'topic' ,T(fr.miage.sid.forum.domain.Permission).WRITE)")
  public ModelAndView createPost(
      @Valid Post post,
      BindingResult result,
      @PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("post/create");
      return modelAndView;
    }

    postService.save(post, topicId);
    
    mailService.sendNotificationEmail(topicService.getOne(topicId).getFollowers(), post.getCreatedBy(),topicService.getOne(topicId));
    
    modelAndView.setViewName("redirect:/topic/" + topicId);
    
    return modelAndView;
  }
}
