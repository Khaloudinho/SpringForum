package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private final UserService userService;
    private final ProjectService projectService;
    private final TopicService topicService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, ProjectService projectService, TopicService topicService, PostService postService) {
        this.userService = userService;
        this.projectService = projectService;
        this.topicService = topicService;
        this.postService = postService;
    }

    @GetMapping("/users")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView("user/show");
        List<User> allUsers = userService.getAll();

        Map numberOfProjectByUser = new HashMap<User, Integer>();
        Map numberOfTopicByUser = new HashMap<User, Integer>();
        Map numberOfPostByUser = new HashMap<User, Integer>();
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
}
