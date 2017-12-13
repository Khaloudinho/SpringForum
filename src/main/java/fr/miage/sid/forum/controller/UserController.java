package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.repository.UserRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

  private final UserRepository userRepository;
  @Autowired
  public UserController(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @RequestMapping("/register")
  public String registerUser(User user) {
    return "registerUser";
  }

  @PostMapping("/register")
  public String registerUser(@Valid User user, BindingResult bindingResult){
    if (bindingResult.hasErrors()) {
      return "registerUser";
    } else {
      userRepository.save(user);
      return "redirect:/";
    }
  }

}
