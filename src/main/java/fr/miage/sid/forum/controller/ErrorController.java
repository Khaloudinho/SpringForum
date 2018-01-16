package fr.miage.sid.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ErrorController {

  @GetMapping("/403")
  public String error403() {
    log.warn("In error controller : working");
    return "error/403";
  }

}
