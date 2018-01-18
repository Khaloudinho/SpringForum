package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import javax.validation.Valid;

import fr.miage.sid.forum.service.UserService;
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
  private final UserService userService;

  @Autowired
  public PostController(PostService postService,
                        UserService userService) {
    this.postService = postService;
    this.userService = userService;
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

    postService.save(post, topicId);
    modelAndView.setViewName("redirect:/topic/" + topicId);

    return modelAndView;
  }

  @GetMapping("/post/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getPostUpdateForm(Post post, @PathVariable("postId") Long postId, @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView("post/update");
    if (!postService.exists(postId)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This post doesn't exist");
    }

    modelAndView.addObject(post);
    modelAndView.addObject("currentPost", postService.getOne(postId));
    return modelAndView;
  }

  @PostMapping("/post/{postId}")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView updatePost(
          @Valid Post post,
          BindingResult result,
          @PathVariable("postId") Long postId,
          @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (!postService.exists(postId)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This post doesn't exist");
    }

    if (result.hasErrors()) {
      modelAndView.addObject("currentPost", postService.getOne(postId));
      modelAndView.setViewName("post/update");
      return modelAndView;
    }

    if (!postService.exists(postId)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This post doesn't exist");
    }

    boolean isAdmin = false;
    for (Role role : userService.getOne(principal.getId()).getRoles()) {
      if(role.getRole().equals("ROLE_ADMIN")){isAdmin = true;}
    }
    if (!(userService.getOne(principal.getId()).equals(postService.getOne(postId).getCreatedBy())
            || isAdmin)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "This is not your post ! :)");
    }

    Post originalPost = postService.getOne(postId);
    postService.save(originalPost.setContent(post.getContent()));
    modelAndView.setViewName("redirect:/topic/" + originalPost.getTopic().getId());

    return modelAndView;
  }
}
