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
 * A ExtVideo.
 */
@Entity
@Table(name = "ext_video")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "extvideo")
public class ExtVideo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "is_youtube", nullable = false)
    private Boolean isYoutube;

    @OneToOne(mappedBy = "extVideo")
    @JsonIgnore
    private Tutorial extVideoTutorial;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsYoutube() {
        return isYoutube;
    }

    public void setIsYoutube(Boolean isYoutube) {
        this.isYoutube = isYoutube;
    }

    public Tutorial getExtVideoTutorial() {
        return extVideoTutorial;
    }

    public void setExtVideoTutorial(Tutorial tutorial) {
        this.extVideoTutorial = tutorial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtVideo extVideo = (ExtVideo) o;

        if ( ! Objects.equals(id, extVideo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtVideo{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", url='" + url + "'" +
            ", isYoutube='" + isYoutube + "'" +
            '}';
    }
}
