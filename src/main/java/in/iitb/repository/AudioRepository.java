package in.iitb.repository;

import in.iitb.domain.Audio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Audio entity.
 */
public interface AudioRepository extends JpaRepository<Audio,Long> {

}
