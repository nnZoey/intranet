package vn.leap.intranet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.leap.intranet.domain.Employee;

/**
 * Spring Data SQL repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT p FROM Employee e, Person p where e.id = p.employee")
    List<Object> findAllEmployee();
}
