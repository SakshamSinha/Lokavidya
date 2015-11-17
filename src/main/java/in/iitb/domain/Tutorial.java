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
 * A Tutorial.
 */
@Entity
@Table(name = "tutorial")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tutorial")
public class Tutorial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    private Project tutProj;

    @OneToOne    private User user;

    @OneToOne    private Project project;

    @OneToMany(mappedBy = "commentTutorial")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToOne    private ExtVideo extVideo;

    @OneToOne(mappedBy = "tutorial")
    @JsonIgnore
    private SegmentVideo tutorialSVideo;

    @ManyToOne
    private Ownership ownersProject;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getTutProj() {
        return tutProj;
    }

    public void setTutProj(Project project) {
        this.tutProj = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public ExtVideo getExtVideo() {
        return extVideo;
    }

    public void setExtVideo(ExtVideo extVideo) {
        this.extVideo = extVideo;
    }

    public SegmentVideo getTutorialSVideo() {
        return tutorialSVideo;
    }

    public void setTutorialSVideo(SegmentVideo segmentVideo) {
        this.tutorialSVideo = segmentVideo;
    }

    public Ownership getOwnersProject() {
        return ownersProject;
    }

    public void setOwnersProject(Ownership ownership) {
        this.ownersProject = ownership;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tutorial tutorial = (Tutorial) o;

        if ( ! Objects.equals(id, tutorial.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tutorial{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
