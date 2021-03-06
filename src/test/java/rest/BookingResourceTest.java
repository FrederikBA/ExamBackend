package rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import dtos.Assistant.AssistantDTO;
import dtos.Booking.BookingDTO;
import entities.Assistant;
import entities.Role;
import entities.Booking;
import entities.Car;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class BookingResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Booking b1, b2, b3;
    private static Car c1, c2, c3;
    private static User u1;
    private static Assistant a1, a2, a3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    public void setUp() {
        EntityManager em = emf.createEntityManager();
        b1 = new Booking(50);
        b2 = new Booking(25);
        b3 = new Booking(15);

        c1 = new Car("XXYY333", "TestModelOne", "TestMakeOne", 2020);
        c2 = new Car("YYXX444", "TestModelTwo", "TestMakeTwo", 2021);
        c3 = new Car("XYYX555", "TestModelThree", "TestMakeThree", 2022);

        a1 = new Assistant("TestNameOne", "Danish", 5, 125);
        a2 = new Assistant("TestNameTwo", "Swedish", 2, 115);
        a3 = new Assistant("TestNameThree", "Polish", 27, 75);

        Role userRole = new Role("user");
        u1 = new User("user", "test");
        u1.addRole(userRole);

        c1.addBooking(b1);
        c2.addBooking(b2);
        c3.addBooking(b3);

        u1.addBooking(b1);
        u1.addBooking(b2);
        u1.addBooking(b3);

        b1.addAssistant(a1);
        b2.addAssistant(a2);
        b3.addAssistant(a3);

        try {
            em.getTransaction().begin();
            em.createQuery("delete from Assistant").executeUpdate();
            em.createQuery("delete from Booking").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(userRole);
            em.persist(u1);
            em.persist(b1);
            em.persist(b2);
            em.persist(b3);
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    //TODO: Something is wrong with the objects configured in this class, tests return nullpointers.
    /*
    @Test
    public void testCreateBooking() {
        Booking b4 = new Booking(1000);
        Car c4 = new Car("TRXD654", "T-Roc", "Volkswagen", 2018);
        b4.setCar(c4);
        b4.addAssistant(a1);
        b4.addAssistant(a2);
        u1.addBooking(b4);


        given()
                .contentType("application/json")
                .body(new BookingDTO(b4))
                .when()
                .post("booking")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("duration", equalTo("1000"));
    }

    @Test
    public void testDeleteBooking() {
        given()
                .contentType("application/json")
                .pathParam("id", b3.getId())
                .delete("booking/{id}")
                .then()
                .assertThat()
                .statusCode(200);

        List<BookingDTO> allBookings;

        allBookings = given()
                .contentType("application/json")
                .when()
                .get("/booking/all")
                .then()
                .extract().body().jsonPath().getList("booking", BookingDTO.class);

        BookingDTO b3DTO = new BookingDTO(b3);

        assertThat(allBookings, not(hasItem(b3DTO)));
    }
*/
}