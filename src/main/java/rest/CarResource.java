package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.Assistant.AssistantDTO;
import dtos.Car.CarDTO;
import facades.BookingFacade;
import facades.CarFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/car")
public class CarResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CarFacade facade = CarFacade.getInstance(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        return gson.toJson(facade.getAllCars());
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String editCar(@PathParam("id") int id, String car) {
        CarDTO c = gson.fromJson(car, CarDTO.class);
        c.setId(id);
        CarDTO cEdited = facade.editCar(c);
        return gson.toJson(cEdited);
    }
}