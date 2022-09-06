package cz.osu.kip.jwttokendemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

  @Autowired
  private AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<AppUser> user = appUserRepository.get(username);
    if (user.isEmpty())
      throw new UsernameNotFoundException("Username " + username + " unknown.");
    UserDetails ret = new AppUserDetails(
            user.get().getEmail(),
            user.get().getPassword(),
            user.get().getRoles().toArray(String[]::new));
    return ret;
  }
}
