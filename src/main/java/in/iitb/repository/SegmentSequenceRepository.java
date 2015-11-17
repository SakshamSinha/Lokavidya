package in.iitb.repository;

import in.iitb.domain.SegmentSequence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SegmentSequence entity.
 */
public interface SegmentSequenceRepository extends JpaRepository<SegmentSequence,Long> {

}
