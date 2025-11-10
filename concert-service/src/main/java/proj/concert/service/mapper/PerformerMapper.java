package proj.concert.service.mapper;

import proj.concert.service.domain.Performer;
import proj.concert.common.dto.PerformerDTO;



public class PerformerMapper {
    public static Performer toDomainObject(PerformerDTO performerdto) {
        Performer performer = new Performer(
                performerdto.getId(),
                performerdto.getName(),
                performerdto.getImageName(),
                performerdto.getGenre(),
                performerdto.getBlurb());
        return performer;

    }

    public static PerformerDTO toDTO(Performer performer) {
        PerformerDTO performerdto = new PerformerDTO(
                performer.getId(),
                performer.getName(),
                performer.getImageName(),
                performer.getGenre(),
                performer.getBlurb());
        return performerdto;
    }
}
