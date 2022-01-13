package facades;

import dtos.Assistant.AssistantDTO;
import dtos.Car.CarDTO;
import dtos.Car.CarsDTO;
import entities.Assistant;
import entities.Car;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class CarFacade {
    private static EntityManagerFactory emf;
    private static CarFacade instance;

    private CarFacade() {
    }

    public static CarFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CarFacade();
        }
        return instance;
    }

    public CarsDTO getAllCars() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c", Car.class);
            List<Car> result = query.getResultList();
            return new CarsDTO(result);
        } finally {
            em.close();
        }
    }

    public CarDTO editCar(CarDTO carDTO) {
        EntityManager em = emf.createEntityManager();

        if (carDTO.getRegNumber().length() == 0 || carDTO.getMake().length() == 0 || carDTO.getModel().length() == 0) {
            throw new WebApplicationException("Inputs are missing, please make sure to fill out the formular");
        }

        try {
            em.getTransaction().begin();
            Car car = em.find(Car.class, carDTO.getId());
            if (car == null) {
                throw new WebApplicationException("No car found matching the id");
            } else {
                car.setRegNumber(carDTO.getRegNumber());
                car.setModel(carDTO.getModel());
                car.setMake(carDTO.getMake());
                if (carDTO.getYear() <= 1992) {
                    throw new WebApplicationException("This car is too old to drive in Denmark");
                } else if (carDTO.getYear() > 2022) {
                    throw new WebApplicationException("This car was not made yet since the given year is in the future");
                } else {
                    car.setYear(carDTO.getYear());
                }
            }
            em.getTransaction().commit();
            return new CarDTO(car);
        } finally {
            em.close();
        }
    }
}
