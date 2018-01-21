package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import java.util.HashSet;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class TopicController {

  private final TopicService topicService;
  private final PostService postService;
  private final UserService userService;
  private final ProjectService projectService;

  @Autowired
  public TopicController(TopicService topicService,
      PostService postService,
      UserService userService, ProjectService projectService) {
    this.topicService = topicService;
    this.postService = postService;
    this.userService = userService;
    this.projectService = projectService;
  }

  @GetMapping("project/{projectId}/topic/create")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getTopicCreateForm(Topic topic, @PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView("topic/create");
//    Project project = projectService.getOne(projectId);
    modelAndView.addObject(topic);
    modelAndView.addObject("projectId", projectId);
    return modelAndView;
  }

  @PostMapping("project/{projectId}/topic")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView createTopic(
      @Valid Topic topic,
      BindingResult result,
      String postContent,
      @CurrentUser MyPrincipal principal,
      @PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("topic/create");
      modelAndView.addObject("errorPostContent", postContent);
      return modelAndView.addObject("projectId", projectId);
    }

    topic.addFollower(userService.getOne(principal.getId()));
    Topic createdTopic = topicService.save(topic, projectId);

    if (createdTopic == null || postContent.equals("")) {
      ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND,
          "This project does not exist, making a new topic is impossible");
      return modelAndView;
    }

    postService.save(new Post().setContent(postContent), createdTopic.getId());
    modelAndView.setViewName("redirect:/project/" + projectId);

    return modelAndView;
  }

  @GetMapping("/topic/{topicId}")
  public ModelAndView showTopic(@PathVariable("topicId") Long topicId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    Topic topic = topicService.getOne(topicId);
    modelAndView.setViewName("topic/show");
    modelAndView.addObject("topic", topic);
    modelAndView.addObject("posts", postService.getAllByTopic(topic));
    if (principal != null) {
      modelAndView.addObject("userIsAdmin", principal.isAdmin());
      modelAndView.addObject("currentUser", userService.getOne(principal.getId()));
    }

    return modelAndView;
  }

  @GetMapping("topic/{topicId}/edit")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView editTopic(@PathVariable("topicId") String topicId) {
    ModelAndView modelAndView = new ModelAndView();
//
//    if (!(postService.isCreator(principal.getId(), originalPost) || principal.isAdmin())) {
//      return ViewUtils
//          .setErrorView(modelAndView, HttpStatus.FORBIDDEN, "This is not your post ! :)");
//    }

    Topic topic = topicService.getOne(Long.valueOf(topicId));

    HashSet<User> tmpReader = new HashSet<>();
    HashSet<User> tmpWriter = new HashSet<>();

    topic.getReaders().forEach((reader) -> tmpReader.add(userService.getOne(reader)));
    topic.getWriters().forEach((writer) -> tmpWriter.add(userService.getOne(writer)));

    log.info("Readers size: " + tmpReader.size());
    log.info("Writers size: " + tmpWriter.size());
    modelAndView.setViewName("topic/edit");
    modelAndView.addObject("topic", topic);
    modelAndView.addObject("users", userService.getAll());
    modelAndView.addObject("usersReader", tmpReader);
    modelAndView.addObject("usersWriter", tmpWriter);

    return modelAndView;
  }
}
