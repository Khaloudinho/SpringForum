package fr.miage.sid.forum.config.security;

import fr.miage.sid.forum.domain.HasPermissions;
import fr.miage.sid.forum.domain.Permission;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  private TopicService topicService;

  @Autowired
  private ProjectService projectService;

  @Override
  public boolean hasPermission(Authentication authentication, Object domainObject,
      Object permission) {
    // Don't use this method signature
    return true;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    if (!targetType.equals("topic") && !targetType.equals("project")
        || !(permission instanceof Permission)) {
      return true;
    }

    HasPermissions entity;
    Long principalId = null;

    if (targetType.equals("topic")) {
      entity = topicService.getOne((Long) targetId);
    } else {
      entity = projectService.getOne((Long) targetId);
    }

    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      principalId = ((MyPrincipal) authentication.getPrincipal()).getId();
    }

    log.info("Checking permission" + permission + "for type: " + targetType + " for userId: "
        + principalId);

    return entity.hasPermission(principalId, (Permission) permission);
  }
}
