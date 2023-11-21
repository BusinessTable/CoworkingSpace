package ch.zli.m223.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Credential;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class SessionService {

  @Inject
  UserService userService;

  public Response authenticate(Credential credential) {
    Optional<ApplicationUser> principal = userService.findByEmail(credential.getEmail());
    StringBuilder result = new StringBuilder();

    try {
      // Hash password
      final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
      final byte[] hashbytes = digest.digest(
          credential.getPassword().getBytes(StandardCharsets.UTF_8));
      for (byte aByte : hashbytes) {
        result.append(String.format("%02x", aByte));
      }
    } catch (Exception e) {
      System.err.println("Couldn't hash password.");
    }

    try {
      if (principal.isPresent() && principal.get().getPassword().equals(result.toString())) {
        String token = Jwt
            .issuer("https://zli.example.com/")
            .upn(credential.getEmail())
            .groups(new HashSet<>(Arrays.asList(principal.get().getUserType().getTitle())))
            .expiresIn(Duration.ofDays(720))
            .claim("userID", principal.get().getId())
            .sign();
        return Response
            .ok(principal.get())
            .cookie(new NewCookie("punchclock", token))
            .header("Authorization", "Bearer " + token)
            .build();
      }
    } catch (Exception e) {
      System.err.println("Couldn't validate password.");
    }

    return Response.status(Response.Status.FORBIDDEN).build();
  }
}
