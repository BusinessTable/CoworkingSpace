package ch.zli.m223.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.model.UserType;
import ch.zli.m223.service.UserTypeService;

@Path("/userTypes")
@Tag(name = "UserType", description = "Handling of userTypes")
@RolesAllowed({ "ApplicationUser", "Admin" })
public class UserTypeController {
    
    @Inject
    UserTypeService userTypeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Index all categories.", 
        description = "Returns a list of all categories."
    )
    public List<UserType> index() {
        return userTypeService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new userType.", 
        description = "Creates a new userType and returns the newly added userType."
    )
    public UserType create(UserType userType) {
       return userTypeService.createUserType(userType);
    }

    @Path("/{id}")
    @DELETE
    @Operation(
        summary = "Deletes an userType.",
        description = "Deletes an userType by its id."
    )
    public void delete(@PathParam("id") Long id) {
        userTypeService.deleteUserType(id);
    }

    @Path("/{id}")
    @PUT
    @Operation(
        summary = "Updates an userType.",
        description = "Updates an userType by its id."
    )
    public UserType update(@PathParam("id") Long id, UserType userType) {
        return userTypeService.updateUserType(id, userType);
    }
}
