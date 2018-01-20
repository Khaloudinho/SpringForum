package fr.miage.sid.forum.config.security;

import fr.miage.sid.forum.domain.Permission;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionService {

  @Autowired
  private TopicService topicService;

  @Autowired
  private ProjectService projectService;

  public boolean canReadTopic(Long topicId) {
    Topic topic = topicService.getOne(topicId);
    return topic.hasPermission(getPrincipalId(), Permission.READ);
  }


  public boolean canWriteTopic(Long topicId) {
    log.info("Test if can write Topic");
    Topic topic = topicService.getOne(topicId);
    return topic.hasPermission(getPrincipalId(), Permission.WRITE);
  }

  public boolean canReadProject(Long projectId) {
    Project project = projectService.getOne(projectId);
    return project.hasPermission(getPrincipalId(), Permission.READ);
  }

  public boolean canWriteProjetct(Long projectId) {
    Project project = projectService.getOne(projectId);
    return project.hasPermission(getPrincipalId(), Permission.WRITE);
  }

  private Long getPrincipalId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      return ((MyPrincipal) authentication.getPrincipal()).getId();
    }

    return null;
  }

}
