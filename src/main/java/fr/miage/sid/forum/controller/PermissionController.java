package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Permission;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* Handles permissions on topics and projects
*/
@RestController
@Slf4j
public class PermissionController {

  private TopicService topicService;
  private ProjectService projectService;

  @Autowired
  public PermissionController(TopicService topicService,
      ProjectService projectService) {
    this.topicService = topicService;
    this.projectService = projectService;
  }

  /**
  * Adds permision to a user on a topic
  */
  @GetMapping("/topic/permission/{topicId}")
  public Map<Permission, Boolean> addTopicPermission(@PathVariable("topicId") Long topicId,
      @RequestParam("permission") String permission, @RequestParam("userId") Long userId) {
    log.info("Adding " + permission + " to current user on topic");
    Topic topic = topicService.getOne(topicId);
    Map<Permission, Boolean> result = topic
        .givePermissionTo(userId, Permission.valueOf(permission));
    topicService.save(topic, topic.getProject().getId());
    return result;
  }

  /**
  * Adds permision to a user on a project
  */
  @GetMapping("/project/permission/{projectId}")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public Map<Permission, Boolean> addProjectPermission(@PathVariable("projectId") Long projectId,
      @RequestParam("permission") String permission, @RequestParam("userId") Long userId) {
    log.info("Adding " + permission + " to current user on project");
    Project project = projectService.getOne(projectId);
    Map<Permission, Boolean> result = project
        .givePermissionTo(userId, Permission.valueOf(permission));
    projectService.save(project);
    return result;
  }


  /**
  * Deletes permision to a user on a topic
  */
  @DeleteMapping("/topic/permission/{topicId}")
  public Map<Permission, Boolean> revokeTopicPermission(@PathVariable("topicId") Long topicId,
      @RequestParam("permission") String permission, @RequestParam("userId") Long userId) {
    log.info("Revoking " + permission + " to current user on topic");
    Topic topic = topicService.getOne(topicId);
    Map<Permission, Boolean> result = topic
        .removePermissionOf(userId, Permission.valueOf(permission));
    topicService.save(topic, topic.getProject().getId());
    return result;
  }


  /**
  * Deletes permision to a user on a project
  */
  @DeleteMapping("/project/permission/{projectId}")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public Map<Permission, Boolean> removeProjectPermission(@PathVariable("projectId") Long projectId,
      @RequestParam("permission") String permission, @RequestParam("userId") Long userId) {
    log.info("Revoking " + permission + " to current user on project");
    Project project = projectService.getOne(projectId);
    Map<Permission, Boolean> result = project
        .removePermissionOf(userId, Permission.valueOf(permission));
    projectService.save(project);
    return result;
  }

}
