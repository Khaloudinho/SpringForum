package fr.miage.sid.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

/**
* Utility class to create view used a lot in code
* in exemple setErrorView creates an error view in one line instead of 4.
*/
public class ViewUtils {

  public static ModelAndView setErrorView(ModelAndView modelAndView, HttpStatus httpStatus,
      String message) {
    modelAndView.setViewName("error/basic");
    modelAndView.setStatus(httpStatus);
    modelAndView.addObject("errorCode", httpStatus);
    modelAndView.addObject("message", message);
    return modelAndView;
  }

}
