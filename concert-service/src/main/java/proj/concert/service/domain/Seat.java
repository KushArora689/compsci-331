package proj.concert.service.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.dto.SeatDTO;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import proj.concert.common.types.BookingStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
public class Seat{
	@Id
	@GeneratedValue()
	private long id;
	private String label;
	private boolean isBooked = false;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date;
	private BigDecimal price;

	private BookingStatus bookingStatus = BookingStatus.Any;

	public Seat() {
	}

	public Seat(String label, BigDecimal price) {
		this.label = label;
		this.price = price;
	}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}

	public long getId() { return id; }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean getIsBooked() { return isBooked; }

	public void setIsBooked(boolean isBooked) { this.isBooked = isBooked; }

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	public LocalDateTime getDate() {return date; }

	public void setDate(LocalDateTime date) {this.date = date; }

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BookingStatus getBookingStatus() { return bookingStatus; }

	public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Seat seat = (Seat) o;

		return new EqualsBuilder()
				.append(id, seat.id)
				.append(label, seat.label)
				.append(price, seat.price)
				.append(bookingStatus, seat.bookingStatus)
				.append(date, seat.date)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(label)
				.append(price)
				.append(bookingStatus)
				.append(date)
				.toHashCode();
	}

}
