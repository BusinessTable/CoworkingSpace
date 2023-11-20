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

import ch.zli.m223.model.SpaceType;
import ch.zli.m223.service.SpaceTypeService;

@Path("/spaceTypes")
@Tag(name = "SpaceTypes", description = "Handling of spaceTypes")
@RolesAllowed({"Admin"})
public class SpaceTypeController {
    
    @Inject
    SpaceTypeService spaceTypeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Index all categories.", 
        description = "Returns a list of all categories."
    )
    public List<SpaceType> index() {
        return spaceTypeService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new spaceType.", 
        description = "Creates a new spaceType and returns the newly added spaceType."
    )
    public SpaceType create(SpaceType spaceType) {
       return spaceTypeService.createSpaceType(spaceType);
    }

    @Path("/{id}")
    @DELETE
    @Operation(
        summary = "Deletes an spaceType.",
        description = "Deletes an spaceType by its id."
    )
    public void delete(@PathParam("id") Long id) {
        spaceTypeService.deleteSpaceType(id);
    }

    @Path("/{id}")
    @PUT
    @Operation(
        summary = "Updates an spaceType.",
        description = "Updates an spaceType by its id."
    )
    public SpaceType update(@PathParam("id") Long id, SpaceType spaceType) {
        return spaceTypeService.updateSpaceType(id, spaceType);
    }
}
