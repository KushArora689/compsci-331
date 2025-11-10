package proj.concert.service.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.dto.SeatDTO;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BOOKING")
public class Booking {


    private long concertId;
    @Id
    @GeneratedValue
    private long bkId;
    private long userId;
    private LocalDateTime date;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private Set<Seat> seats = new HashSet<>();

    public Booking() {
    }

    public Booking(long concertId, LocalDateTime date, Set<Seat> seats) {
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    public LocalDateTime getDate() {
        return date;
    }
    public long getBookingId() {return this.bkId;}
    public long getUserId() {return this.userId;}


    public void setUserId(long userId) {this.userId = userId;}
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

}
