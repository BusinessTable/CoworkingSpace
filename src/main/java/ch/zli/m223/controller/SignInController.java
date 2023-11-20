package ch.zli.m223.controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.service.SignInService;

@Path("/signin")
@Tag(name = "Sign In", description = "Handling of Signins")
@PermitAll
public class SignInController {
    @Inject
    SignInService sessionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Register a user.", description = "Returns a token upon successful authentication.")
    public Response create(ApplicationUser applicationUser) {
        return this.sessionService.authenticate(applicationUser);
    }
}