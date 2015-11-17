package in.iitb.repository;

import in.iitb.domain.Tutorial;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tutorial entity.
 */
public interface TutorialRepository extends JpaRepository<Tutorial,Long> {

}
