package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.service.PostService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class PostController {

  private final PostService postService;
  private JmsTemplate jmsTemplate;

  @Autowired
  public PostController(JmsTemplate jmsTemplate, PostService postService) {
    this.jmsTemplate = jmsTemplate;
    this.postService = postService;
  }

  /**
   * get form to create a post on a topic
   *
   * @PreAuthorize is a Spring Security tool that filter requests to a method
   * In this case, a user can only get the form if he is authentificated and if he has the right to write in the topic
   */
  @GetMapping("/topic/{topicId}/post/create")
  @PreAuthorize("isAuthenticated() and @permissionService.canWriteTopic(#topicId)")
  public ModelAndView getPostCreateForm(Post post, @PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView("post/create");
    modelAndView.addObject(post);
    modelAndView.addObject("topicId", topicId);
    return modelAndView;
  }

  /**
   * Creating a post, the user and date are automaticaly set, validated with @Valid
   */
  @PostMapping("/topic/{topicId}/post")
  @PreAuthorize("isAuthenticated() and @permissionService.canWriteTopic(#topicId)")
  public ModelAndView createPost(
      @Valid Post post,
      BindingResult result,
      @PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("post/create");
      return modelAndView;
    }

    Post saved = postService.save(post, topicId);
    // sending message to JMS mail Queue for sending later
    jmsTemplate.convertAndSend("mailQueue", saved);
    modelAndView.setViewName("redirect:/topic/" + topicId);

    return modelAndView;
  }

  /**
   * Return the form used to edit a post
   */
  @GetMapping("/post/{postId}/update")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getPostUpdateForm(@PathVariable("postId") Long postId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView("post/update");

    Post originalPost = postService.getOne(postId);

    if (isNotPostCreatorOrAdmin(principal, originalPost)) {
      return ViewUtils
          .setErrorView(modelAndView, HttpStatus.FORBIDDEN, "This is not your post ! :)");
    }

    modelAndView.addObject("currentPost", originalPost);
    return modelAndView;
  }


  /**
   * Modify an existing post
   */
  @PutMapping("/post/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView updatePost(
      @Valid Post post,
      BindingResult result,
      @PathVariable("postId") Long postId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.addObject("currentPost", postService.getOne(postId));
      modelAndView.setViewName("post/update");
      return modelAndView;
    }

    Post originalPost = postService.getOne(postId);

    if (isNotPostCreatorOrAdmin(principal, originalPost)) {
      return ViewUtils
          .setErrorView(modelAndView, HttpStatus.FORBIDDEN, "This is not your post ! :)");
    }

    postService.save(originalPost.setContent(post.getContent()));
    modelAndView.setViewName("redirect:/topic/" + originalPost.getTopic().getId());

    return modelAndView;
  }

  private boolean isNotPostCreatorOrAdmin(MyPrincipal principal, Post originalPost) {
    return !postService.isCreator(principal.getId(), originalPost) && !principal.isAdmin();
  }
}
