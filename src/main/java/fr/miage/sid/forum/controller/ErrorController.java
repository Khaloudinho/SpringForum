package fr.miage.sid.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

  /**
   * Custom 403 handling
   */
  @GetMapping("/403")
  public String error403() {
    return "error/403";
  }

}
