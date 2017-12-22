package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.service.TopicService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopicController {

  private final TopicService topicService;

  @Autowired
  public TopicController(TopicService topicService) {
    this.topicService = topicService;
  }

  @GetMapping("project/{projectId}/newtopic")
  public ModelAndView getTopicForm(Topic topic, @PathVariable("projectId") String projectId){
    ModelAndView modelAndView = new ModelAndView("topic/new");
    modelAndView.addObject(topic);
    modelAndView.addObject("projectId", projectId);
    return modelAndView;
  }

  @PostMapping("project/{projectId}/newtopic")
  public ModelAndView createTopic(@Valid Topic topic, BindingResult result, @PathVariable("projectId") String projectId){
    ModelAndView modelAndView = new ModelAndView();
    if(result.hasErrors()){
      modelAndView.setViewName("topic/new");
      modelAndView.addObject("projectId", projectId);
    } else {
      topicService.save(topic, projectId);
      modelAndView.setViewName("redirect:/");
    }
    return modelAndView;
  }
}
