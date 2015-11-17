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
 * A Segment.
 */
@Entity
@Table(name = "segment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "segment")
public class Segment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "sync", nullable = false)
    private Boolean sync;

    @ManyToOne
    private SegmentVideo segmentSegmentVideo;

    @OneToOne(mappedBy = "segment")
    @JsonIgnore
    private Video segmentVideo;

    @OneToOne(mappedBy = "segment")
    @JsonIgnore
    private Image segmentImage;

    @OneToOne(mappedBy = "segment")
    @JsonIgnore
    private Audio segmentAudio;

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

    public SegmentVideo getSegmentSegmentVideo() {
        return segmentSegmentVideo;
    }

    public void setSegmentSegmentVideo(SegmentVideo segmentVideo) {
        this.segmentSegmentVideo = segmentVideo;
    }

    public Video getSegmentVideo() {
        return segmentVideo;
    }

    public void setSegmentVideo(Video video) {
        this.segmentVideo = video;
    }

    public Image getSegmentImage() {
        return segmentImage;
    }

    public void setSegmentImage(Image image) {
        this.segmentImage = image;
    }

    public Audio getSegmentAudio() {
        return segmentAudio;
    }

    public void setSegmentAudio(Audio audio) {
        this.segmentAudio = audio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Segment segment = (Segment) o;

        if ( ! Objects.equals(id, segment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Segment{" +
            "id=" + id +
            ", sync='" + sync + "'" +
            '}';
    }
}
