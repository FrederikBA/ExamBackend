package dtos.Booking;

import entities.Booking;

import java.util.List;
import java.util.Objects;

public class BookingsDTO {
    private List<BookingDTO> bookings;

    public BookingsDTO(List<Booking> bookings) {
        this.bookings = BookingDTO.getFromList(bookings);
    }

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingsDTO that = (BookingsDTO) o;
        return Objects.equals(bookings, that.bookings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookings);
    }

    @Override
    public String toString() {
        return "BookingsDTO{" +
                "bookings=" + bookings +
                '}';
    }
}
