package fr.miage.sid.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ErrorController {

  @GetMapping("/403")
  public String error403() {
    log.warn("In error controller : working");
//    ModelAndView modelAndView = new ModelAndView();
//    ViewUtils.setErrorView(modelAndView, HttpStatus.FORBIDDEN, "You do not have the rights to access this resource");
    return "error/403";
  }

}
