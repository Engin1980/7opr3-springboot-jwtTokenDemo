package cz.osu.kip.jwttokendemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired private AppUserRepository appUserRepository;

  @GetMapping("/list")
  public ResponseEntity<List<String>> listUsers() {
    List<String> ret = appUserRepository.getAll().stream()
            .filter(q -> q.getRoles().contains("USER"))
            .map(q -> q.getEmail())
            .sorted()
            .toList();
    return ResponseEntity.ok(ret);
  }

  @GetMapping("/listAdmin")
  public ResponseEntity<List<String>> listAdmins() {
    List<String> ret = appUserRepository.getAll().stream()
            .filter(q -> q.getRoles().contains("ADMIN"))
            .map(q -> q.getEmail())
            .sorted()
            .toList();
    return ResponseEntity.ok(ret);
  }

  @Autowired private PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity signUp(String email, String password) {
    AppUser appUser = new AppUser(
            email,
            passwordEncoder.encode(password),
            "USER");
    appUserRepository.add(appUser);
    return ResponseEntity.ok(null);
  }

  @Autowired  private AuthenticationManager authenticationManager;
  @Autowired  private JwtTokenUtil jwtTokenUtil;

  @PostMapping("/login")
  public ResponseEntity<String> login(String email, String password){
    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (DisabledException | BadCredentialsException e) {
      return ResponseEntity.badRequest().body("Invalid credentials.");
    }
    final UserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
    final String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity.ok(token);
  }

}
