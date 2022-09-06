package cz.osu.kip.jwttokendemo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppUser {
  private String email;
  private String password;
  private final Set<String> roles = new HashSet<>();

  public AppUser(String email, String password, String... roles) {
    this.email = email;
    this.password = password;
    this.roles.addAll(Arrays.asList(roles));
  }

  public AppUser() {  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Set<String> getRoles() {
    return new HashSet<>(this.roles);
  }
}
