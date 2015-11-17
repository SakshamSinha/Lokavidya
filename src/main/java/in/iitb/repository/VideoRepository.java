package in.iitb.repository;

import in.iitb.domain.Video;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Video entity.
 */
public interface VideoRepository extends JpaRepository<Video,Long> {

}
