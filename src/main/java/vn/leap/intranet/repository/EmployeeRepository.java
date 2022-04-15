package vn.leap.intranet.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.leap.intranet.domain.Employee;

/**
 * Spring Data SQL repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT e FROM Employee e, User u where e.userId = u.id and u.id = ?1")
    Optional<Employee> findByUserId(Long id);
}
