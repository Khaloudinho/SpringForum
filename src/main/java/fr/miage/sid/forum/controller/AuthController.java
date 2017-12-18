package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.UserService;
import java.security.Principal;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

  private Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final UserService userService;

  @Autowired
  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public ModelAndView getLoginForm(Principal principal) {
    ModelAndView modelAndView = new ModelAndView("auth/login");
    if (principal != null) {
      modelAndView.setViewName("redirect:/");
    }
    return modelAndView;
  }

  @GetMapping("/register")
  public ModelAndView getRegisterForm(User user) {
    ModelAndView modelAndView = new ModelAndView("auth/register");
    modelAndView.addObject(user);
    return modelAndView;
  }

  @PostMapping("/register")
  public ModelAndView register(@Valid User user, BindingResult result) {
    ModelAndView modelAndView = new ModelAndView();
    if (result.hasErrors()) {
      modelAndView.setViewName("auth/register");
    } else {
      userService.save(user);
      modelAndView.setViewName("redirect:/");
    }

    return modelAndView;
  }

}
