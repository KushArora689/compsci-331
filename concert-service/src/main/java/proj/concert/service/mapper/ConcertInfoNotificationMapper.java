package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertInfoNotificationDTO;
import proj.concert.service.domain.ConcertInfoNotification;

public class ConcertInfoNotificationMapper {

    public static ConcertInfoNotification toDomainObject(ConcertInfoNotificationDTO concertnotidto) {
        ConcertInfoNotification concertinfonotifcation = new ConcertInfoNotification(
                concertnotidto.getNumSeatsRemaining());
        return concertinfonotifcation;
    }

    public static ConcertInfoNotificationDTO toDTO(ConcertInfoNotification concertinfonotification) {
        ConcertInfoNotificationDTO concertnotidto = new ConcertInfoNotificationDTO(
                concertinfonotification.getNumSeatsRemaining());
        return concertnotidto;
    }
}
