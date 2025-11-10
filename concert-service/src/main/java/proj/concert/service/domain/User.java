package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Version
    @Column(name="VERSION", nullable = false)
    private Long version;

    @Column(name = "UId")
    private String UId = null;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userId")
    private List<Booking> bookings = new ArrayList<>();


    protected User() {}
    public User(String username, String password) {
    }

    public User(Long id,String username, String password, Long version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    public void addBooking(Booking booking) { bookings.add(booking); }

    public Long getId() {
        return id;
    }
    public String getUId() { return UId; }
    public void setUId(String uuid) { this.UId = uuid; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(username, user.username)
                .append(password, user.password)
                .append(id, user.id)
                .append(version, user.version)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(password)
                .append(version)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id + '\'' +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", version=" + version + '\'' +
                '}';
    }
}
