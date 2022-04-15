package vn.leap.intranet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.leap.intranet.domain.Assignment;

/**
 * Spring Data SQL repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {
    @Query("SELECT a FROM Assignment a where (SELECT e.id from Employee e, User u where u.id = e.userId.id and u.id = ?1) = a.employee.id")
    List<Assignment> findUserAssignmentsById(Long id);
}
