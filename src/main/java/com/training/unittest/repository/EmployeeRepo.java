package com.training.unittest.repository;

import com.training.unittest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByJPQLNameParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "select * from Employee e where e.first_name=?1 and e.last_name=?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);
}
