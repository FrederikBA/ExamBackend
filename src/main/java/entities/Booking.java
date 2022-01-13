package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Date created;
    private double duration;
    @ManyToOne
    private User user;

    @ManyToOne
    private Car car;

    @ManyToMany(mappedBy = "bookings", cascade = CascadeType.PERSIST)
    private List<Assistant> assistants;

    public Booking() {
    }

    public Booking(double duration) {
        this.created = new Date();
        this.duration = duration;
        this.assistants = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Assistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<Assistant> assistants) {
        this.assistants = assistants;
    }

    public void addAssistant(Assistant assistant) {
        if (assistant != null) {
            this.assistants.add(assistant);
            assistant.getBookings().add(this);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
