package fr.miage.sid.forum.config.security;

public interface MyPrincipal {

  Long getId();

  String getName();

  String getEmail();

  boolean isAdmin();

  boolean isFromDB();
}
