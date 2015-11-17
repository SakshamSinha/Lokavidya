package in.iitb.repository;

import in.iitb.domain.Ownership;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ownership entity.
 */
public interface OwnershipRepository extends JpaRepository<Ownership,Long> {

    @Query("select ownership from Ownership ownership where ownership.ownershipUser.login = ?#{principal.username}")
    List<Ownership> findByOwnershipUserIsCurrentUser();

}
