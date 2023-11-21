package ch.zli.m223.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hibernate.mapping.Collection;

import ch.zli.m223.model.Booking;
import ch.zli.m223.service.BookingService;
import ch.zli.m223.service.UserService;

@Path("/bookings")
@Tag(name = "Bookings", description = "Handling of bookings")
public class BookingController {

    @Inject
    BookingService bookingService;

    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all entries.", description = "Returns a list of all entries.")
    public List<Booking> index(@Context SecurityContext ctx) {
        Optional<Long> userID = jwt.claim("userID");

        boolean isAdmin = userService.findAll().stream().filter(user -> user.getId() == userID.get())
                .anyMatch(user -> user.getUserType().getId() == 1);

        if (isAdmin) {
            return bookingService.findAll();
        }

        return bookingService.findAll().stream()
                .filter(booking -> booking.getUser().getId() == userID.get())
                .filter(booking -> booking.getEndDate().isAfter(booking.getStartDate()))
                .collect(Collectors.toList());
    }

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @GET
    @Path("/archivedBookings")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "returns all bookings from the past.", description = "returns all bookings from the past.")
    public List<Booking> getArchivedBookings(@Context SecurityContext ctx) {
        Optional<Long> userID = jwt.claim("userID");

        boolean isAdmin = userService.findAll().stream().filter(user -> user.getId() == userID.get())
                .anyMatch(user -> user.getUserType().getId() == 1);

        if (isAdmin) {
            return bookingService.findAll().stream()
                .filter(booking -> booking.getEndDate().isAfter(booking.getStartDate())).collect(Collectors.toList());
        }

        return bookingService.findAll().stream()
                .filter(booking -> booking.getUser().getId() == userID.get())
                .filter(booking -> booking.getEndDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new booking.", description = "Creates a new booking and returns the newly added booking.")
    public Booking create(@Valid Booking booking) {
        return bookingService.createBooking(booking);
    }

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes an booking.", description = "Deletes an booking by its id.")
    public void delete(@PathParam("id") Long id) {
        Optional<Long> userID = jwt.claim("userID");

        boolean isAdmin = userService.findAll().stream().filter(user -> user.getId() == userID.get())
                .anyMatch(user -> user.getUserType().getId() == 1);

        boolean isBookingOfUser = bookingService.findAll().stream().filter(cBooking -> id == cBooking.getId())
                .anyMatch(cBooking -> userID.get() == cBooking.getUser().getId());

        boolean isInFunture = bookingService.findAll().stream().filter(cBooking -> id == cBooking.getId())
                .anyMatch(cBooking -> cBooking.getStartDate().isAfter(LocalDateTime.now()));
        if (isAdmin) {
            bookingService.deleteBooking(id);
        }
        if (isBookingOfUser && isInFunture) {
            bookingService.deleteBooking(id);
        }
    }

    @RolesAllowed({ "ApplicationUser", "Admin" })
    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates an booking.", description = "Updates an booking by its id.")
    public Booking update(@PathParam("id") Long id, @Valid Booking booking) {

        Optional<Long> userID = jwt.claim("userID");

        boolean isAdmin = userService.findAll().stream().filter(user -> user.getId() == userID.get())
                .anyMatch(user -> user.getUserType().getId() == 1);

        boolean isBookingOfUser = bookingService.findAll().stream().filter(cBooking -> id == cBooking.getId())
                .anyMatch(cBooking -> userID.get() == cBooking.getUser().getId());

        if (isAdmin) {
            return bookingService.updateBooking(id, booking);
        }
        if (isBookingOfUser) {
            return bookingService.updateBooking(id, booking);
        }
        return null;
    }
}
