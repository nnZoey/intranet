package vn.leap.intranet.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.leap.intranet.domain.Employee;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT e FROM Employee e, User u where e.userId = u.id and u.id = ?1")
    Optional<Employee> findByUserId(Long id);
    
}
