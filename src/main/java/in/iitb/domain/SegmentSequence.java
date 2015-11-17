package in.iitb.domain;

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
 * A SegmentSequence.
 */
@Entity
@Table(name = "segment_sequence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "segmentsequence")
public class SegmentSequence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "segmentsequence", nullable = false)
    private String segmentsequence;

    @OneToOne    private SegmentVideo segmentVideo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegmentsequence() {
        return segmentsequence;
    }

    public void setSegmentsequence(String segmentsequence) {
        this.segmentsequence = segmentsequence;
    }

    public SegmentVideo getSegmentVideo() {
        return segmentVideo;
    }

    public void setSegmentVideo(SegmentVideo segmentVideo) {
        this.segmentVideo = segmentVideo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SegmentSequence segmentSequence = (SegmentSequence) o;

        if ( ! Objects.equals(id, segmentSequence.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SegmentSequence{" +
            "id=" + id +
            ", segmentsequence='" + segmentsequence + "'" +
            '}';
    }
}
