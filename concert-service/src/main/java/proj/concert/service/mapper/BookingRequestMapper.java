package proj.concert.service.mapper;

import proj.concert.common.dto.BookingRequestDTO;
import proj.concert.service.domain.BookingRequest;

public class BookingRequestMapper {

    public static BookingRequest toDomainModel(BookingRequestDTO bookingrequestdto) {
        BookingRequest bookingrequest = new BookingRequest(
                bookingrequestdto.getConcertId(),
                bookingrequestdto.getDate(),
                bookingrequestdto.getSeatLabels());
        return bookingrequest;
    }

    public static BookingRequestDTO toDTO(BookingRequest bookingrequest) {
        BookingRequestDTO bookingrequestdto = new BookingRequestDTO(
                bookingrequest.getConcertId(),
                bookingrequest.getDate(),
                bookingrequest.getSeatLabels());
        return bookingrequestdto;
    }

}
