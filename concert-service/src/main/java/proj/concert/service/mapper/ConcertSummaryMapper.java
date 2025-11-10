package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.ConcertSummary;

public class ConcertSummaryMapper {
    public static ConcertSummary toDomainObject(ConcertSummaryDTO concertsummarydto) {
        ConcertSummary concertsummary = new ConcertSummary(
                concertsummarydto.getId(),
                concertsummarydto.getTitle(),
                concertsummarydto.getImageName());
        return concertsummary;
    }
    public static ConcertSummaryDTO toDTO(Concert concertsummary) {
        ConcertSummaryDTO concertsummarydto = new ConcertSummaryDTO(
                concertsummary.getId(),
                concertsummary.getTitle(),
                concertsummary.getImageName());
        return concertsummarydto;
    }
}
