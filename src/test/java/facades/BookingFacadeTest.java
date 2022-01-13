package facades;

import dtos.Booking.BookingDTO;
import entities.Booking;
import entities.Car;
import entities.Role;
import entities.User;
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

class BookingFacadeTest {
    private static EntityManagerFactory emf;
    private static BookingFacade facade;
    private static Booking b1, b2, b3;
    private static User u1;
    private static Car c1, c2, c3;

    public BookingFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookingFacade.getInstance(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from Assistant").executeUpdate();
        em.createQuery("delete from Booking").executeUpdate();
        em.createQuery("delete from Car").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        em.createQuery("delete from Role").executeUpdate();
        em.getTransaction().commit();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        b1 = new Booking(500);
        b2 = new Booking(400);
        b3 = new Booking(750);

        c1 = new Car("XXYY333", "TestModelOne", "TestMakeOne", 2020);
        c2 = new Car("YYXX444", "TestModelTwo", "TestMakeTwo", 2021);
        c3 = new Car("XYYX555", "TestModelThree", "TestMakeThree", 2022);

        Role userRole = new Role("user");
        u1 = new User("user", "test");
        u1.addRole(userRole);

        u1.addBooking(b1);
        u1.addBooking(b2);
        u1.addBooking(b3);

        c1.addBooking(b1);
        c2.addBooking(b2);
        c3.addBooking(b3);


        try {
            em.getTransaction().begin();
            em.createQuery("delete from Assistant").executeUpdate();
            em.createQuery("delete from Booking").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(userRole);
            em.persist(u1);
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
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

    @Test
    public void getBookingsByUserTest() {
        List<BookingDTO> bookings = facade.getAllBookings(u1.getUserName()).getBookings();

        int expected = 3;
        int actual = bookings.size();

        assertEquals(expected, actual);

        BookingDTO b1DTO = new BookingDTO(b1);
        BookingDTO b2DTO = new BookingDTO(b2);
        BookingDTO b3DTO = new BookingDTO(b3);

        assertThat(bookings, containsInAnyOrder(b1DTO,b2DTO, b3DTO));

    }
}