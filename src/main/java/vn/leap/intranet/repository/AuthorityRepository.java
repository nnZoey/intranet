package vn.leap.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.leap.intranet.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
