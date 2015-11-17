package in.iitb.repository;

import in.iitb.domain.ExtVideo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtVideo entity.
 */
public interface ExtVideoRepository extends JpaRepository<ExtVideo,Long> {

}
