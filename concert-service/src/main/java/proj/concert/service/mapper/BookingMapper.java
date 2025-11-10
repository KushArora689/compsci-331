package proj.concert.service.mapper;

import proj.concert.common.dto.BookingDTO;
import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Booking;
import proj.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingMapper {

    public static Booking toDomainObject(BookingDTO bookingdto) {
        Set<Seat> seats = new HashSet<>();
        for (SeatDTO seatdto : bookingdto.getSeats() ) {
            Seat s = SeatMapper.toDomainObject(seatdto); {
                s.setIsBooked(true);
                s.setDate(bookingdto.getDate());
                seats.add(s);
            }
        }

        Booking booking = new Booking(
                bookingdto.getConcertId(),
                bookingdto.getDate(),
                seats);

        return booking;
    }

    public static BookingDTO toDTO(Booking booking) {
        List<SeatDTO> seats = new ArrayList<>();
        for (Seat seat : booking.getSeats()) {
            seats.add(SeatMapper.toDTO(seat));
        }

        BookingDTO bookingdto = new BookingDTO(
                booking.getConcertId(),
                booking.getDate(),
                seats);

        return bookingdto;
    }
}
