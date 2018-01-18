package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.UserDetailsImpl;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.validation.UserForm;
import fr.miage.sid.forum.service.UserService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class AuthController {

  private final UserService userService;

  @Autowired
  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public ModelAndView getLoginForm(Principal principal) {
    ModelAndView modelAndView = new ModelAndView("auth/login");
    if (principal != null) {
      // Redirect if user already logged in
      modelAndView.setViewName("redirect:/");
    }
    return modelAndView;
  }

  @GetMapping("/register")
  public ModelAndView getRegisterForm(UserForm user) {
    ModelAndView modelAndView = new ModelAndView("auth/register");
    modelAndView.addObject(user);
    return modelAndView;
  }

  @PostMapping("/register")
  public ModelAndView register(@Valid UserForm userForm, BindingResult result) {
    ModelAndView modelAndView = new ModelAndView();
    ModelMapper mapper = new ModelMapper();
    User user = mapper.map(userForm, User.class);
    if (result.hasErrors()) {
      modelAndView.setViewName("auth/register");
    } else {
      User saved = userService.save(user);
      signin(userService.getUserDetails(saved));
      modelAndView.setViewName("redirect:/");
    }

    return modelAndView;
  }

  private void signin(UserDetailsImpl user) {
    SecurityContextHolder
        .getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
  }

}
