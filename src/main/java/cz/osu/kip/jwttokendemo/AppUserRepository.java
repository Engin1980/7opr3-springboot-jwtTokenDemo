package cz.osu.kip.jwttokendemo;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AppUserRepository {
  private final Set<AppUser> users = new HashSet<>();

  public void add(AppUser user) {
    this.users.add(user);
  }

  public Optional<AppUser> get(String email) {
    return users.stream().filter(q -> q.getEmail().equalsIgnoreCase(email)).findFirst();
  }

  public Set<AppUser> getAll() {
    return new HashSet<>(this.users);
  }
}
