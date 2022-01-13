package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.Assistant.AssistantDTO;
import dtos.Booking.BookingDTO;
import facades.AssistantFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/assistant")
public class AssistantResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AssistantFacade facade = AssistantFacade.getInstance(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String getAll() {
        return gson.toJson(facade.getAllAssistants());
    }

    //Implemented for admin frontend feature for US-4
    @Path("/unprotectedall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUnprotected() {
        return gson.toJson(facade.getAllAssistants());
    }

    @Path("/booking/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssistantsByBookingId(@PathParam("id") int id) {
        return gson.toJson(facade.getAssistantsByBooking(id));
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createAssistant(String assistant) {
        AssistantDTO a = gson.fromJson(assistant, AssistantDTO.class);
        AssistantDTO aNew = facade.createAssistant(a);
        return gson.toJson(aNew);
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String editAssistants(@PathParam("id") int id, String assistant) {
        AssistantDTO a = gson.fromJson(assistant, AssistantDTO.class);
        a.setId(id);
        AssistantDTO aEdited = facade.editAssistant(a);
        return gson.toJson(aEdited);
    }
}