package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Retrieve all employees matching a given status.
     * When status = 1, retrieves all active employees.
     */
    List<Employee> findByStatus(Integer status);

    /**
     * Check if an employee ID already exists (ignoring case).
     */
    boolean existsByEmployeeIdIgnoreCase(String employeeId);

    /**
     * Check if an employee ID exists for another record (useful for update validation).
     */
    boolean existsByEmployeeIdIgnoreCaseAndIdNot(String employeeId, Long id);

    /**
     * Search active employees by ID or partial/full name.
     * Case-insensitive substring search matching either 'name' or 'employeeId'.
     * Returns all matching active employees (status = 1).
     */
    @Query("SELECT e FROM Employee e WHERE e.status = :status AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Employee> searchByKeywordAndStatus(@Param("query") String query, @Param("status") Integer status);

    /**
     * Find active employee by Employee ID code (e.g. EMP101).
     */
    Optional<Employee> findByEmployeeIdIgnoreCaseAndStatus(String employeeId, Integer status);
}
