package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    /**
     * Retrieve all active employees (where status == 1).
     */
    List<EmployeeDTO> getAllActiveEmployees();

    /**
     * Retrieve a specific employee by primary ID.
     */
    EmployeeDTO getEmployeeById(Long id);

    /**
     * Retrieve a specific employee by Employee ID code (e.g. EMP101).
     */
    EmployeeDTO getEmployeeByEmployeeId(String employeeId);

    /**
     * Add a new employee record.
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    /**
     * Edit / update an existing employee's details (name, employeeId, timeIn, timeOut, salary).
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    /**
     * Soft delete an employee by updating status to 0.
     */
    void softDeleteEmployee(Long id);

    /**
     * Completely remove an employee from the database (hard delete).
     */
    void hardDeleteEmployee(Long id);

    /**
     * Completely remove all employees from the database (hard delete).
     *
     * @return the number of employee records deleted
     */
    long hardDeleteAllEmployees();

    /**
     * Search employees by Employee ID or partial/full name.
     * Returns all matching active employees.
     */
    List<EmployeeDTO> searchEmployees(String keyword);

    /**
     * Retrieve inactive (soft-deleted, status == 0) employees.
     */
    List<EmployeeDTO> getInactiveEmployees();

    /**
     * Restore a soft-deleted employee back to active status (status = 1).
     */
    EmployeeDTO restoreEmployee(Long id);
}
