package ch.zli.m223.controller;

import java.time.LocalDateTime;
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

import ch.zli.m223.model.Space;
import ch.zli.m223.service.SpaceService;

@Path("/spaces")
@Tag(name = "Spaces", description = "Handling of spaces")
public class SpaceController {

    @Inject
    SpaceService spaceService;

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all categories.", description = "Returns a list of all categories.")
    public List<Space> index() {
        return spaceService.findAll();
    }

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @GET
    @Path("/reservedSpaces")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all categories.", description = "Returns a list of all categories.")
    public Float getReservedSpaces() {
        List<Space> spaces = spaceService.findAll();
        int spaceCount = spaces.size();
        int[] bookedSpaceCount = { 0 };
        spaces.stream().forEach(space -> {
            if (space.getBookingId().stream().filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()))
                    .filter(booking -> booking.getEndDate().isAfter(LocalDateTime.now())).count() > 0) {
                bookedSpaceCount[0]++;
            }
        });
        return Float.valueOf((bookedSpaceCount[0] / spaceCount) * 100);
    }

    @RolesAllowed({ "Admin" })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new space.", description = "Creates a new space and returns the newly added space.")
    public Space create(Space space) {
        return spaceService.createSpace(space);
    }

    @RolesAllowed({ "Admin" })
    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes an space.", description = "Deletes an space by its id.")
    public void delete(@PathParam("id") Long id) {
        spaceService.deleteSpace(id);
    }

    @RolesAllowed({ "Admin" })
    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates an space.", description = "Updates an space by its id.")
    public Space update(@PathParam("id") Long id, Space space) {
        return spaceService.updateSpace(id, space);
    }
}
