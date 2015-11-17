package in.iitb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import in.iitb.domain.enumeration.OwnershipType;

import in.iitb.domain.enumeration.Rights;

/**
 * A Ownership.
 */
@Entity
@Table(name = "ownership")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ownership")
public class Ownership implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OwnershipType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "right")
    private Rights right;

    @OneToMany(mappedBy = "ownersAre")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "ownersProject")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tutorial> tutorials = new HashSet<>();

    @ManyToOne
    private User ownershipUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OwnershipType getType() {
        return type;
    }

    public void setType(OwnershipType type) {
        this.type = type;
    }

    public Rights getRight() {
        return right;
    }

    public void setRight(Rights right) {
        this.right = right;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Tutorial> getTutorials() {
        return tutorials;
    }

    public void setTutorials(Set<Tutorial> tutorials) {
        this.tutorials = tutorials;
    }

    public User getOwnershipUser() {
        return ownershipUser;
    }

    public void setOwnershipUser(User user) {
        this.ownershipUser = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ownership ownership = (Ownership) o;

        if ( ! Objects.equals(id, ownership.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ownership{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", right='" + right + "'" +
            '}';
    }
}
