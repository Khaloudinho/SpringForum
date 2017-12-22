package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.security.MyPrincipal;
import java.security.Principal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This a sample controller so you can see the difference between MyPrincipal and Principal
 */
// TODO Delete this when everybody has understand Principal notions
@RestController
public class UserController {

  @RequestMapping("/user")
  public Principal user(@AuthenticationPrincipal MyPrincipal principal, Principal elPrincipal) {
    return elPrincipal;
  }
}
