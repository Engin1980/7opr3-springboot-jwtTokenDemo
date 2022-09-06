package cz.osu.kip.jwttokendemo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Authorization");

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      String jwtToken = requestTokenHeader.substring(7);

      boolean isJwtTokenValid = false;
      try {
        isJwtTokenValid = jwtTokenUtil.isTokenValid(jwtToken);
      } catch (SignatureException ex) {
        logger.info("JWT token signature issue");
      } catch (MalformedJwtException ex){
        logger.info("JWT token invalid format");
      } catch (PrematureJwtException ex){
        logger.info("JWT token accessed before its validity.");
      } catch (ExpiredJwtException ex){
        logger.info("JWT token expired.");
      }

      if (isJwtTokenValid) {
        String userName = jwtTokenUtil.getClaimsFromToken(jwtToken).getSubject();
        String[] roles = jwtTokenUtil.getRolesFromToken(jwtToken);
        UserDetails userDetails = new AppUserDetails(userName, null, roles);
        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(upat);
      } else {
        logger.info("JWT token invalid.");
      }
    } else {
      logger.warn("JWT Token does not begin with Bearer String");
    }
    filterChain.doFilter(request, response);
  }
}
