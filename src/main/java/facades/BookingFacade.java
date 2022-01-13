package facades;

import javax.persistence.EntityManagerFactory;

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
}
