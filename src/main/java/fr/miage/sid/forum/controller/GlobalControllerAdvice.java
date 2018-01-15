package fr.miage.sid.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(NumberFormatException.class)
  public ModelAndView stringIdsNotAllowed() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "String ids are not allowed");
  }
}
