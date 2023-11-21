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
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class SignInService {
    @Inject
    UserService userService;
    @Inject
    UserTypeService userTypeService;

    public Response authenticate(ApplicationUser user) {
        Optional<ApplicationUser> principal = userService.findByEmail(user.getEmail());

        int countOfUsers = userService.findAll().size();

        try {
            if (principal == null || !principal.isPresent()) {

                // Create user, if no user exsists create admin
                ApplicationUser createdUser = userService.createUser(user);
                if (countOfUsers == 0) {
                    createdUser.setUserType(userTypeService.findAll().get(0));
                    userService.updateUser(createdUser.getId(), createdUser);
                }

                // Hash password
                final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
                final byte[] hashbytes = digest.digest(
                        createdUser.getPassword().getBytes(StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                for (byte aByte : hashbytes) {
                    result.append(String.format("%02x", aByte));
                }

                createdUser.setPassword(result.toString());

                String token = Jwt
                        .issuer("https://zli.example.com/")
                        .upn(createdUser.getEmail())
                        .groups(new HashSet<>(Arrays.asList(createdUser.getUserType().getTitle())))
                        .expiresIn(Duration.ofHours(24))
                        .claim("userID", createdUser.getId())
                        .sign();

                return Response
                        .ok(createdUser)
                        .cookie(new NewCookie("punchclock", token))
                        .header("Authorization", "Bearer " + token)
                        .build();
            }
        } catch (Exception e) {
            System.err.println("Couldn't create user.");
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
