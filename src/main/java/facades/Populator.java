/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.Assistant;
import entities.Booking;
import entities.Car;
import entities.User;
import utils.EMF_Creator;


public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        //User user = em.find(User.class, "user");
        //Car car = new Car("MD21233", "UP", "Volkswagen", 2022);
        //Booking booking = new Booking(500);
        //Assistant assistant = new Assistant("Jens", "Danish", 5, 125);
        Assistant assistantTwo = new Assistant("Sven", "Swedish", 2, 115);
        Assistant assistantThree = new Assistant("Jadwig", "Polish", 17, 65);

        //booking.addAssistant(assistant);
        //user.addBooking(booking);

       // car.addBooking(booking);

        em.getTransaction().begin();
        //em.persist(car);
        //em.persist(booking);
        //em.persist(assistant);
        em.persist(assistantTwo);
        em.persist(assistantThree);
        em.getTransaction().commit();


    }

    public static void main(String[] args) {
        populate();
    }
}
