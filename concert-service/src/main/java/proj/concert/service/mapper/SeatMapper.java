package proj.concert.service.mapper;
import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Seat;


public class SeatMapper {
    public static Seat toDomainObject(SeatDTO seatdto) {
        Seat seat = new Seat(
                seatdto.getLabel(),
                seatdto.getPrice());
        return seat;
    }

    public static SeatDTO toDTO(Seat seat) {
        SeatDTO seatdto = new SeatDTO(
                seat.getLabel(),
                seat.getPrice());
        return seatdto;
    }

}
