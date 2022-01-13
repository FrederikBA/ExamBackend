package dtos.Booking;

import dtos.Assistant.AssistantDTO;
import dtos.Car.CarDTO;
import dtos.UserDTO;
import entities.Assistant;
import entities.Booking;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookingDTO {
    private Integer id;
    private Date created;
    private double duration;
    private CarDTO car;
    private List<AssistantDTO> assistants;
    private String user;

    public static List<BookingDTO> getFromList(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> new BookingDTO(booking))
                .collect(Collectors.toList());
    }

    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.created = booking.getCreated();
        this.duration = booking.getDuration();
        this.car = new CarDTO(booking.getCar());
        this.assistants = AssistantDTO.getFromList(booking.getAssistants());
        this.user = new UserDTO(booking.getUser()).getUserName();
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

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public List<AssistantDTO> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<AssistantDTO> assistants) {
        this.assistants = assistants;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDTO that = (BookingDTO) o;
        return Double.compare(that.duration, duration) == 0 && Objects.equals(id, that.id) && Objects.equals(created, that.created) && Objects.equals(car, that.car) && Objects.equals(assistants, that.assistants) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, duration, car, assistants, user);
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id=" + id +
                ", created=" + created +
                ", duration=" + duration +
                ", car=" + car +
                ", assistants=" + assistants +
                ", user='" + user + '\'' +
                '}';
    }
}
