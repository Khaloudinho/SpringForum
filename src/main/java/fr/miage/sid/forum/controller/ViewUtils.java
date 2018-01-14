package fr.miage.sid.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class ViewUtils {

  public static void setErrorView(ModelAndView modelAndView, HttpStatus httpStatus,
      String message) {
    modelAndView.setViewName("error/basicTemplate");
    modelAndView.setStatus(httpStatus);
    modelAndView.addObject("errorCode", httpStatus);
    modelAndView.addObject("message", message);
  }

}
