package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.domain.validation.UserForm;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
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
public class UserController {

  private final UserService userService;
  private final ProjectService projectService;
  private final TopicService topicService;
  private final PostService postService;

  @Autowired
  public UserController(UserService userService, ProjectService projectService,
      TopicService topicService, PostService postService) {
    this.userService = userService;
    this.projectService = projectService;
    this.topicService = topicService;
    this.postService = postService;
  }

  /**
  * Return a view with details of all users, for admins only
  */
  @GetMapping("/users")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public ModelAndView getAll() {
    ModelAndView modelAndView = new ModelAndView("user/show");
    List<User> allUsers = userService.getAll();

    Map<User, Integer> numberOfProjectByUser = new HashMap<>();
    Map<User, Integer> numberOfTopicByUser = new HashMap<>();
    Map<User, Integer> numberOfPostByUser = new HashMap<>();
    // FIXME If we have time, this is a very inefficient way to do things
    // FIXME If we have 2000 users we will do 2000 * 3 sql queries just to have some fancy numbers
    // FIXME We should be able to have all infos with only one query (but might be a hard query)
    allUsers.forEach(user -> {
      numberOfProjectByUser.put(user, projectService.countCreatedByUser(user));
      numberOfTopicByUser.put(user, topicService.countCreatedByUser(user));
      numberOfPostByUser.put(user, postService.countCreatedByUser(user));
    });

    modelAndView.addObject("users", allUsers);
    modelAndView.addObject("mapProjectCount", numberOfProjectByUser);
    modelAndView.addObject("mapTopicCount", numberOfTopicByUser);
    modelAndView.addObject("mapPostCount", numberOfPostByUser);
    return modelAndView;
  }

  /**
  * Form to edit one's informations, only for one user and admins
  */
  @GetMapping("/user/{userId}/edit")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getEditForm(UserForm userForm, @PathVariable("userId") Long userId, @CurrentUser MyPrincipal principal){
    ModelAndView modelAndView = new ModelAndView();
    User currentUser = userService.getOne(principal.getId());
    if (currentUser.getOrigin().equals(UserOrigin.GOOGLE)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "You can't edit your profile if you're connected with your google account");
    }
    if (!principal.getId().equals(userId)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "You can only edit your profile");
    }

    userForm.setEmail(currentUser.getEmail());
    userForm.setFirstname(currentUser.getFirstname());
    userForm.setLastname(currentUser.getLastname());
    userForm.setUsername(currentUser.getUsername());
    modelAndView.setViewName("user/edit");
    modelAndView.addObject("userForm", userForm);
    modelAndView.addObject("userId", userId);
    return modelAndView;
  }

  @PutMapping("/user/{userId}/edit")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView editUser(@Valid UserForm userForm, BindingResult result, @PathVariable Long userId, @CurrentUser MyPrincipal principal){

    ModelAndView modelAndView = new ModelAndView();
    User currentUser = userService.getOne(principal.getId());
    if (currentUser.getOrigin().equals(UserOrigin.GOOGLE)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "You can't edit your profile if you're connected with your google account");
    }
    if (!principal.getId().equals(userId)){
      return ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "You can only edit your profile");
    }
    if (result.hasErrors()) {
      modelAndView.setViewName("user/edit");
      modelAndView.addObject("userId", userId);
      return modelAndView;
    }
    currentUser.setEmail(userForm.getEmail());
    currentUser.setFirstname(userForm.getFirstname());
    currentUser.setLastname(userForm.getLastname());
    currentUser.setPassword(userForm.getPassword());
    currentUser.setUsername(userForm.getUsername());
    userService.save(currentUser);

    modelAndView.setViewName("redirect:/");
    return modelAndView;
  }

}
