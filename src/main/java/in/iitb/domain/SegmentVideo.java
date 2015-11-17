package in.iitb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SegmentVideo.
 */
@Entity
@Table(name = "segment_video")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "segmentvideo")
public class SegmentVideo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "sync", nullable = false)
    private Boolean sync;

    @OneToMany(mappedBy = "segmentSegmentVideo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Segment> segments = new HashSet<>();

    @OneToOne    private Tutorial tutorial;

    @OneToOne(mappedBy = "segmentVideo")
    @JsonIgnore
    private SegmentSequence segmentVideoSSequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    public SegmentSequence getSegmentVideoSSequence() {
        return segmentVideoSSequence;
    }

    public void setSegmentVideoSSequence(SegmentSequence segmentSequence) {
        this.segmentVideoSSequence = segmentSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SegmentVideo segmentVideo = (SegmentVideo) o;

        if ( ! Objects.equals(id, segmentVideo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SegmentVideo{" +
            "id=" + id +
            ", sync='" + sync + "'" +
            '}';
    }
}
