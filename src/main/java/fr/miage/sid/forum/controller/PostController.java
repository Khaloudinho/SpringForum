package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.service.MailService;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  @PreAuthorize("isAuthenticated() and @permissionService.canWriteTopic(#topicId)")
  public ModelAndView getPostCreateForm(Post post, @PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView("post/create");
    modelAndView.addObject(post);
    modelAndView.addObject("topicId", topicId);
    return modelAndView;
  }

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
    mailService.sendNotifToAllFollowers(saved);
    modelAndView.setViewName("redirect:/topic/" + topicId);

    return modelAndView;
  }

  @GetMapping("/post/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getPostUpdateForm(Post post, @PathVariable("postId") Long postId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView("post/update");
    if (!postService.exists(postId)) {
      return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This post doesn't exist");
    }

    modelAndView.addObject(post);
    modelAndView.addObject("currentPost", postService.getOne(postId));
    return modelAndView;
  }

  @PutMapping("/post/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView updatePost(
      @Valid Post post,
      BindingResult result,
      @PathVariable("postId") Long postId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (!postService.exists(postId)) {
      return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This post doesn't exist");
    }

    if (result.hasErrors()) {
      modelAndView.addObject("currentPost", postService.getOne(postId));
      modelAndView.setViewName("post/update");
      return modelAndView;
    }

    Post originalPost = postService.getOne(postId);
    if (!(postService.isCreator(principal.getId(), originalPost) || principal.isAdmin())) {
      return ViewUtils
          .setErrorView(modelAndView, HttpStatus.FORBIDDEN, "This is not your post ! :)");
    }

    postService.save(originalPost.setContent(post.getContent()));
    modelAndView.setViewName("redirect:/topic/" + originalPost.getTopic().getId());

    return modelAndView;
  }
}
