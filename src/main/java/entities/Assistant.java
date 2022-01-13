package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    private String language;
    private int experience;
    private double pricePrHour;

    @ManyToMany
    private List<Booking> bookings;

    public Assistant() {
    }

    public Assistant(String name, String language, int experience, double pricePrHour) {
        this.name = name;
        this.language = language;
        this.experience = experience;
        this.pricePrHour = pricePrHour;
        this.bookings = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getPricePrHour() {
        return pricePrHour;
    }

    public void setPricePrHour(double pricePrHour) {
        this.pricePrHour = pricePrHour;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
