package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.exception.ProjectNotFoundException;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
* Controller over controllers
* We use it to handle common exceptions
*/
@ControllerAdvice
public class GlobalControllerAdvice {

  /**
  * This execption occurs when the url a user type is broken
  * for exemple /project/one instead of /project/1
  */
  @ExceptionHandler(NumberFormatException.class)
  public ModelAndView stringIdsNotAllowed() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "String ids are not allowed");
  }

  /**
  * This exception occurs when a topic is requested but does not exist
  */
  @ExceptionHandler(TopicNotFoundException.class)
  public ModelAndView topicDoesntExist() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "Topic doesn't exists");
  }


  /**
  * This exception occurs when a project is requested but does not exist
  */
  @ExceptionHandler(ProjectNotFoundException.class)
  public ModelAndView projectDoesntExist() {
    ModelAndView modelAndView = new ModelAndView();
    return ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND,
        "Project doesn't exist");
  }

}
