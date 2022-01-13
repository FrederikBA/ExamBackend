package facades;

import dtos.Assistant.AssistantDTO;
import entities.Assistant;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.jupiter.api.Assertions.*;

class AssistantFacadeTest {

    private static EntityManagerFactory emf;
    private static AssistantFacade facade;
    private Assistant a1, a2, a3;

    public AssistantFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AssistantFacade.getInstance(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        a1 = new Assistant("TestNameOne", "Danish", 5, 125);
        a2 = new Assistant("TestNameTwo", "Swedish", 2, 115);
        a3 = new Assistant("TestNameThree", "Polish", 27, 75);

        try {
            em.getTransaction().begin();
            em.createQuery("delete from Assistant").executeUpdate();
            em.createQuery("delete from Booking").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getAllAssistantsTest() {
        List<AssistantDTO> assistants = facade.getAllAssistants().getAssistants();

        int expected = 3;
        int actual = assistants.size();

        //Test that there is in fact 3 assistants in the list
        assertEquals(expected,actual);

        AssistantDTO a1DTO = new AssistantDTO(a1);
        AssistantDTO a2DTO = new AssistantDTO(a2);
        AssistantDTO a3DTO = new AssistantDTO(a3);

        //Test that it is the objects that we expect the list will contain.
        assertThat(assistants, containsInAnyOrder(a1DTO,a2DTO,a3DTO));

    }

}