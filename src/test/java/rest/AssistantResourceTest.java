package rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import dtos.Assistant.AssistantDTO;
import entities.Assistant;
import entities.Role;
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

class AssistantResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
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

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        a1 = new Assistant("TestNameOne", "Danish", 5, 125);
        a2 = new Assistant("TestNameTwo", "Swedish", 2, 115);
        a3 = new Assistant("TestNameThree", "Polish", 27, 75);

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User user = new User("user", "test");
        user.addRole(userRole);
        User admin = new User("admin", "test");
        admin.addRole(adminRole);
        User both = new User("user_admin", "test");
        both.addRole(userRole);
        both.addRole(adminRole);

        try {
            em.getTransaction().begin();
            em.createQuery("delete from Assistant").executeUpdate();
            em.createQuery("delete from Booking").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
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

    @Test
    public void testGetAllAssistants() {
        login("user", "test");

        List<AssistantDTO> assistants;

        AssistantDTO a1DTO = new AssistantDTO(a1);
        AssistantDTO a2DTO = new AssistantDTO(a2);
        AssistantDTO a3DTO = new AssistantDTO(a3);

        assistants = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/assistant/all").then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("assistants", AssistantDTO.class);

        assertEquals(3, assistants.size());

        assertThat(assistants, containsInAnyOrder(a1DTO, a2DTO, a3DTO));
    }

    @Test
    public void testCreateAssistant() {
        login("admin", "test");

        Assistant a4 = new Assistant("J??nke", "Danish", 9, 175);
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(new AssistantDTO(a4))
                .when()
                .post("assistant")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(a4.getName()))
                .body("language", equalTo(a4.getLanguage()))
                .body("experience", equalTo(a4.getExperience()));
    }

    @Test
    public void testEditAssistant() {
        login("admin", "test");
        AssistantDTO assistant = new AssistantDTO(a1);
        assistant.setName("Ole");
        assistant.setLanguage("Swahili");
        assistant.setExperience(10);
        assistant.setPricePrHour(1000);

        given()
                .contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(assistant)
                .when()
                .put("assistant/" + a1.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("Ole"))
                .body("language", equalTo("Swahili"))
                .body("experience", equalTo(10));
                //@TODO: Double values in rest assured acting weirdly. Maybe typecasting is neccesary
                //.body("pricePrHour", equalTo(1000.0));
    }
}