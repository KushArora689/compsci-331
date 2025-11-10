package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertInfoNotificationDTO;
import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.service.domain.ConcertInfoNotification;
import proj.concert.service.domain.ConcertInfoSubscription;

public class ConcertInfoSubscriptionMapper {

    public static ConcertInfoSubscription toDomainObject(ConcertInfoSubscriptionDTO concertsubdto) {
        ConcertInfoSubscription concertinfosubscription = new ConcertInfoSubscription(
                concertsubdto.getConcertId(),
                concertsubdto.getDate(),
                concertsubdto.getPercentageBooked());
        return concertinfosubscription;
    }

    public static ConcertInfoSubscriptionDTO toDTO(ConcertInfoSubscription concertinfosubscription) {
        ConcertInfoSubscriptionDTO concertsubdto = new ConcertInfoSubscriptionDTO(
                concertinfosubscription.getConcertId(),
                concertinfosubscription.getDate(),
                concertinfosubscription.getPercentageBooked());
        return concertsubdto;
    }
}
