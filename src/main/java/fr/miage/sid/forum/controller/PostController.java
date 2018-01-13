package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.exception.PermissionPostException;
import fr.miage.sid.forum.security.CurrentUser;
import fr.miage.sid.forum.security.MyPrincipal;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  private final UserService userService;

  @Autowired
  public PostController(PostService postService,
      TopicService topicService, UserService userService) {
    this.postService = postService;
    this.topicService = topicService;
    this.userService = userService;
  }

  @GetMapping("/topic/{topicId}/newpost")
  public ModelAndView getPostForm(Post post, @PathVariable("topicId") String topicId) {
    ModelAndView modelAndView = new ModelAndView("topic/newPost");
    modelAndView.addObject(post);
    modelAndView.addObject("topicId", topicId);
    return modelAndView;
  }

  @PostMapping("/topic/{topicId}/newpost")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView createPost(
      @Valid Post post,
      BindingResult result,
      @PathVariable("topicId") String topicId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("topic/newPost");
      modelAndView.addObject("topicId", topicId);
    } else {
      try {
        Post created = postService.save(post, Long.valueOf(topicId), principal.getId());
        modelAndView.setViewName("redirect:/topic/" + topicId);
        if (created == null) {
          modelAndView.setViewName("error/basicTemplate");
          modelAndView.setStatus(HttpStatus.NOT_FOUND);
          modelAndView.addObject("errorCode", "404 Not Found");
          modelAndView
              .addObject("message", "This topic does not exist, making a new post is impossible");
        }
      } catch (PermissionPostException e) {
        modelAndView.setViewName("error/basicTemplate");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.addObject("errorCode", "403 Forbidden");
        modelAndView.addObject("message", "You do not have the permission to make a post here");
        return modelAndView;
      }
    }
    return modelAndView;
  }
}
