package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home(@AuthenticationPrincipal User principal) {
////    System.out.println(authentication.getDetails());
//    if (principal != null && principal instanceof OAuth2Authentication) {
//      System.out.println("hello");
//      OAuth2Authentication auth2Authentication = (OAuth2Authentication) principal;
//      Authentication authentication  = auth2Authentication.getUserAuthentication();
//      System.out.println(authentication.getDetails());
////      Map<String, String> map = new LinkedHashMap<>();
////      map.put("email", details.get("email"));
////      System.out.println(map);
//    }

    System.out.println(principal);
    return "index";
  }

}
