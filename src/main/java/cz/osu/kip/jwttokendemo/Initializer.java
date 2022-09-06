package cz.osu.kip.jwttokendemo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner
{
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AppUserRepository appUserRepository;
  @Autowired private Logger logger;

  @Override
  public void run(String... args) throws Exception {
    logger.info("Creating new users in AppUserRepository");
    appUserRepository.add(new AppUser(
            "tereza.veverkova@email.cz",
            passwordEncoder.encode("orisek"),
            "ADMIN", "USER"));
    appUserRepository.add(new AppUser(
            "jan.navratil@email.cz",
            passwordEncoder.encode("zahrada"),
            "USER"));
    logger.info("Users inserted.");
  }
}
