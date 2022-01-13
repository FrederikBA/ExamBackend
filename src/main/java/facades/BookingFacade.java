package facades;

import dtos.Booking.BookingsDTO;
import entities.Booking;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookingFacade {
    private static EntityManagerFactory emf;
    private static BookingFacade instance;

    private BookingFacade() {
    }

    public static BookingFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookingFacade();
        }
        return instance;
    }

    public BookingsDTO getAllBookings(String username) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.user.userName =:username", Booking.class);
            query.setParameter("username", username);
            List<Booking> result = query.getResultList();
            return new BookingsDTO(result);
        } finally {
            em.close();
        }
    }
}
