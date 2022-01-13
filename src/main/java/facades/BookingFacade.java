package facades;

import dtos.Assistant.AssistantDTO;
import dtos.Booking.BookingDTO;
import dtos.Booking.BookingsDTO;
import entities.Assistant;
import entities.Booking;
import entities.Car;
import entities.User;

import javax.persistence.*;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
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

    public BookingsDTO getAllBookings() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b", Booking.class);
            List<Booking> result = query.getResultList();
            return new BookingsDTO(result);
        } finally {
            em.close();
        }
    }

    public BookingsDTO getBookingsByName(String username) {
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

    public BookingDTO createBooking(BookingDTO bookingDTO) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();

        if (bookingDTO.getCar().getRegNumber().length() == 0 ||bookingDTO.getCar().getModel().length() == 0 || bookingDTO.getCar().getMake().length() == 0) {
            throw new WebApplicationException("Inputs are missing, please make sure to fill out the formular");
        }

        if(bookingDTO.getDuration() < 10) {
            throw new WebApplicationException("The minimum duration of a car wash is 10 minutes");
        }

        if(bookingDTO.getCar().getYear() > 2022 || bookingDTO.getCar().getYear() < 1985) {
            throw new WebApplicationException("Year not valid");
        }

        try {
            //Create the booking
            Booking booking = new Booking(bookingDTO.getDuration());

            //Create Car
            Car car = new Car(bookingDTO.getCar().getRegNumber(), bookingDTO.getCar().getModel(), bookingDTO.getCar().getMake(), bookingDTO.getCar().getYear());

            booking.setCar(car);


            //Add washing assistants
            for (AssistantDTO assistantDTO : bookingDTO.getAssistants()) {
                TypedQuery<Assistant> query = em.createQuery("SELECT a from Assistant a WHERE a.id =:assistantId", Assistant.class);
                query.setParameter("assistantId", assistantDTO.getId());
                Assistant tmpAssistant = query.getSingleResult();
                if (tmpAssistant != null) {
                    booking.addAssistant(tmpAssistant);
                } else {
                    throw new WebApplicationException("This assistant does not exist");
                }
            }

            //Assign user
            User user = em.find(User.class, bookingDTO.getUser());

            user.addBooking(booking);

            em.getTransaction().begin();
            em.persist(booking);
            em.persist(car);
            em.getTransaction().commit();

            return new BookingDTO(booking);
        } finally {
            em.close();
        }
    }


    public BookingDTO updateBookingAssistants(BookingDTO bookingDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Booking booking = em.find(Booking.class, bookingDTO.getId());


            // Edit assistants

            //Clear assistants from the booking before to allow new owners to be set
            em.getTransaction().begin();
            booking.getAssistants().clear();
            em.createNativeQuery("DELETE FROM ASSISTANT_BOOKING WHERE bookings_id = ?").setParameter(1, booking.getId()).executeUpdate();
            em.getTransaction().commit();

            for (int i = 0; i < bookingDTO.getAssistants().size(); i++) {
                AssistantDTO assistantDTO = bookingDTO.getAssistants().get(i);

                try {
                    Assistant foundAssistant = em.createQuery("SELECT a FROM Assistant a WHERE a.id = :assistantId", Assistant.class).setParameter("assistantId", assistantDTO.getId()).getSingleResult();
                    booking.addAssistant(foundAssistant);
                } catch (NoResultException error) {
                    throw new WebApplicationException("Assistant does not exist");
                }
            }

            //Update the booking
            em.getTransaction().begin();
            em.merge(booking);
            em.getTransaction().commit();
            return new BookingDTO(booking);
        } finally {
            em.close();
        }
    }

    public BookingDTO editBooking(BookingDTO bookingDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, bookingDTO.getId());
            if (booking == null) {
                throw new WebApplicationException("No booking found matching the id");
            } else {
                //Update the created to reflect the time of booking update.
                booking.setCreated(new Date());
                if (bookingDTO.getDuration() < 10) {
                    throw new WebApplicationException("The minimum duration of a car wash is 10 minutes");
                } else {
                    booking.setDuration(bookingDTO.getDuration());
                }
            }
            em.getTransaction().commit();
            return new BookingDTO(booking);
        } finally {
            em.close();
        }
    }

    public BookingDTO deleteBooking(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Booking booking = em.find(Booking.class, id);
            if (booking == null) {
                throw new WebApplicationException("No booking found matching the id");
            } else {
                em.getTransaction().begin();
                em.createNativeQuery("DELETE FROM ASSISTANT_BOOKING WHERE bookings_id = ?").setParameter(1, booking.getId()).executeUpdate();
                em.remove(booking);
                em.getTransaction().commit();
                return new BookingDTO(booking);
            }
        } finally {
            em.close();
        }
    }
}
