package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.repository.UserRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

  private final UserRepository userRepository;

  @Autowired
  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/register")
  public String getRegisterPage(User user, Model model) {
    model.addAttribute("user", user);
    return "auth/register";
  }

  @PostMapping("/register")
  public String register(@Valid User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "auth/register";
    } else {
      // TODO Create service
      userRepository.save(user);
      return "redirect:/";
    }
  }

}
