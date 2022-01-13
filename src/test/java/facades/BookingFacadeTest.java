package facades;

import dtos.Assistant.AssistantDTO;
import dtos.Booking.BookingDTO;
import entities.*;
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
    private static Assistant a1, a2, a3;

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

        a1 = new Assistant("TestNameOne", "Danish", 5, 125);
        a2 = new Assistant("TestNameTwo", "Swedish", 2, 115);
        a3 = new Assistant("TestNameThree", "Polish", 27, 75);

        Role userRole = new Role("user");
        u1 = new User("user", "test");
        u1.addRole(userRole);

        u1.addBooking(b1);
        u1.addBooking(b2);
        u1.addBooking(b3);

        c1.addBooking(b1);
        c2.addBooking(b2);
        c3.addBooking(b3);

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
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(b1);
            em.persist(b2);
            em.persist(b3);
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
    public void getBookingsByUserTest() {
        List<BookingDTO> bookings = facade.getBookingsByName(u1.getUserName()).getBookings();

        int expected = 3;
        int actual = bookings.size();

        assertEquals(expected, actual);

        BookingDTO b1DTO = new BookingDTO(b1);
        BookingDTO b2DTO = new BookingDTO(b2);
        BookingDTO b3DTO = new BookingDTO(b3);

        assertThat(bookings, containsInAnyOrder(b1DTO, b2DTO, b3DTO));

    }

    @Test
    public void createBookingTest() {
        Booking b4 = new Booking(1000);
        Car c4 = new Car("TRXD654", "T-Roc", "Volkswagen", 2018);
        b4.setCar(c4);
        b4.addAssistant(a1);
        b4.addAssistant(a2);
        u1.addBooking(b4);
        BookingDTO createdBooking = new BookingDTO(b4);

        BookingDTO b4DTO = facade.createBooking(createdBooking);

        List<BookingDTO> bookings = facade.getBookingsByName(u1.getUserName()).getBookings();

        //Test if the size of the booking arraylist is now 4 instead of 3.
        int expected = 4;
        int actual = bookings.size();
        assertEquals(expected, actual);


        //Confirm that b4DTO (the new booking) has been added to the list of bookings.
        assertThat(bookings, hasItem(b4DTO));
    }

    @Test
    public void deleteBookingTest() {
        facade.deleteBooking(b3.getId());

        List<BookingDTO> allBookings = facade.getAllBookings().getBookings();

        //Confirm that the bookings arraylist is now 2 bookings down from 3.
        assertEquals(2, allBookings.size());

        BookingDTO b1DTO = new BookingDTO(b1);
        BookingDTO b2DTO = new BookingDTO(b2);
        BookingDTO b3DTO = new BookingDTO(b3);

        //Confirm that the book we deleted is not containing in the array
        assertThat(allBookings, not(hasItem(b3DTO)));

    }
}