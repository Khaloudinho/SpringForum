package fr.miage.sid.forum.config.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMyPrincipalSecurityContextFactory.class)
public @interface WithMockMyPrincipal {

  String id() default "2";

  String username() default "test";

  String[] roles() default {"ROLE_USER"};

  String password() default "password";
}
