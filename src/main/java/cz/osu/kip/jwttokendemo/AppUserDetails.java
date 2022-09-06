package cz.osu.kip.jwttokendemo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AppUserDetails implements UserDetails {

  private final String password;
  private final String userName;
  private final Set<GrantedAuthority> roles = new HashSet<>();

  public AppUserDetails(String userName, String password, String... roles) {
    this.password = password;
    this.userName = userName;
    Arrays.stream(roles)
            .map(q -> new SimpleGrantedAuthority(q))
            .forEach(q -> this.roles.add(q));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
