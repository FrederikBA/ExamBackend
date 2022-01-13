package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String regNumber;
    private String model;
    private String make;
    private int year;
    @OneToMany(mappedBy = "car", cascade = CascadeType.PERSIST)
    private List<Booking> bookings;

    public Car() {
    }

    public Car(String regNumber, String model, String make, int year) {
        this.regNumber = regNumber;
        this.model = model;
        this.make = make;
        this.year = year;
        this.bookings = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        if (booking != null) {
            booking.setCar(this);
        }
    }
}
