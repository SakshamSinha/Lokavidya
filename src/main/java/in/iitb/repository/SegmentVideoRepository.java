package in.iitb.repository;

import in.iitb.domain.SegmentVideo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SegmentVideo entity.
 */
public interface SegmentVideoRepository extends JpaRepository<SegmentVideo,Long> {

}
