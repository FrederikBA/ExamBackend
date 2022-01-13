package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.Booking.BookingDTO;
import facades.AssistantFacade;
import facades.BookingFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/booking")
public class BookingResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final BookingFacade facade = BookingFacade.getInstance(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String getBookingsByUser(@PathParam("username") String username) {
        return gson.toJson(facade.getAllBookings(username));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createBooking(String booking) {
        BookingDTO b = gson.fromJson(booking, BookingDTO.class);
        BookingDTO bNew = facade.createBooking(b);
        return gson.toJson(bNew);
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String editBookingAssistants(@PathParam("id") int id, String booking) {
       BookingDTO b = gson.fromJson(booking, BookingDTO.class);
        b.setId(id);
        BookingDTO bEdited = facade.updateBookingAssistants(b);
        return gson.toJson(bEdited);
    }
}