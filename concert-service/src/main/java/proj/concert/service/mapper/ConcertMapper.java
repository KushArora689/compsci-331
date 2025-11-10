package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertMapper {
    public static Concert toDomainObject(ConcertDTO concertdto) {
        Concert concert = new Concert(
                concertdto.getId(),
                concertdto.getTitle(),
                concertdto.getImageName(),
                concertdto.getBlurb());
        return concert;
    }

    public static ConcertDTO toDTO(Concert concert) {
        ConcertDTO concertdto = new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb());

        List<PerformerDTO> performers = new ArrayList<>();
        for (Performer p : concert.getPerformers()) { performers.add(PerformerMapper.toDTO(p)); }

        concertdto.setPerformers(performers);
        List<LocalDateTime> dates = new ArrayList<>();
        for (LocalDateTime d: concert.getDates()) { dates.add(d); }
        concertdto.setDates(dates);

        return concertdto;
    }

}
