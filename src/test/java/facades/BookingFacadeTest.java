package facades;

import entities.Booking;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.jupiter.api.Assertions.*;

class BookingFacadeTest {
    private static EntityManagerFactory emf;
    private static BookingFacade facade;
    private static Booking b1, b2, b3;

    public BookingFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookingFacade.getInstance(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        b1 = new Booking(500);
        b1 = new Booking(400);
        b1 = new Booking(750);


        try {
            em.getTransaction().begin();
            em.createQuery("delete from Assistant").executeUpdate();
            em.createQuery("delete from Booking").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.persist(b1);
            em.persist(b2);
            em.persist(b3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }
}