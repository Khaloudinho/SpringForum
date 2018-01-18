package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.exception.ProjectNotFoundException;
import fr.miage.sid.forum.exception.TopicNotFoundException;
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

  @ExceptionHandler(TopicNotFoundException.class)
  public ModelAndView topicDoesntExist() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "Topic doesn't exists");
  }


  @ExceptionHandler(ProjectNotFoundException.class)
  public ModelAndView projectDoesntExist() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND,
        "Project doesn't exist");
  }

}
