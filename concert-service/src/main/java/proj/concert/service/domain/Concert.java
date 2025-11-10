package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

@Entity
@Table(name = "CONCERTS")
public class Concert{

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "IMAGE_NAME", nullable = false)
    private String imageName;

    @Column(name = "BLURB", columnDefinition = "LONGTEXT") // default length is too short for some of the really long ones
    private String blurb;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCERT_DATES", joinColumns = { @JoinColumn(name = "CONCERT_ID") } )
    @Column(name = "DATE", nullable = false)
    private Set<LocalDateTime> dates = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "CONCERT_PERFORMER",
            joinColumns = @JoinColumn(name = "CONCERT_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID"))
    private List<Performer> performers = new ArrayList<>();

    public Concert() {
    }

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Concert(String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    public Set<LocalDateTime> getDates() {
        return dates;
    }
    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }



}
