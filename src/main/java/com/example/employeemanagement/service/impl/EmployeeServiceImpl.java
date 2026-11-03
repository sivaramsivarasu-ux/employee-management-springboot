package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.BadRequestException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Show all active employees where status == 1.
     * Demonstrates both query-level and programmatic filtering.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllActiveEmployees() {
        // Retrieve active records from database where status = 1
        List<Employee> activeEmployees = employeeRepository.findByStatus(1);

        // Explicit verification: ensure only employees marked as 1 are displayed
        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee emp : activeEmployees) {
            if (emp.getStatus() != null && emp.getStatus() == 1) {
                result.add(convertToDTO(emp));
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCaseAndStatus(employeeId.trim(), 1)
                .orElseThrow(() -> new ResourceNotFoundException("Active employee not found with Employee ID: " + employeeId));
        return convertToDTO(employee);
    }

    /**
     * New employee registration.
     */
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Validate unique employee ID
        if (employeeRepository.existsByEmployeeIdIgnoreCase(employeeDTO.getEmployeeId().trim())) {
            throw new BadRequestException("Employee ID '" + employeeDTO.getEmployeeId() + "' is already in use.");
        }

        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeId().trim().toUpperCase());
        employee.setName(employeeDTO.getName().trim());
        employee.setTimeIn(employeeDTO.getTimeIn().trim());
        employee.setTimeOut(employeeDTO.getTimeOut().trim());
        employee.setSalary(employeeDTO.getSalary());
        employee.setStatus(1); // Set marked as 1 (active)

        Employee saved = employeeRepository.save(employee);
        return convertToDTO(saved);
    }

    /**
     * Edit employee information and update it so subsequent fetches return updated data.
     */
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Validate unique employee ID if it changed
        String updatedEmpId = employeeDTO.getEmployeeId().trim().toUpperCase();
        if (employeeRepository.existsByEmployeeIdIgnoreCaseAndIdNot(updatedEmpId, id)) {
            throw new BadRequestException("Employee ID '" + updatedEmpId + "' is already in use by another record.");
        }

        employee.setEmployeeId(updatedEmpId);
        employee.setName(employeeDTO.getName().trim());
        employee.setTimeIn(employeeDTO.getTimeIn().trim());
        employee.setTimeOut(employeeDTO.getTimeOut().trim());
        employee.setSalary(employeeDTO.getSalary());
        if (employeeDTO.getStatus() != null) {
            employee.setStatus(employeeDTO.getStatus());
        }

        Employee updated = employeeRepository.save(employee);
        return convertToDTO(updated);
    }

    /**
     * Soft delete: Marks employee status as 0 in the database.
     * When show is called, only employees marked as 1 will be displayed.
     */
    @Override
    public void softDeleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus(0); // Marked as 0
        employeeRepository.save(employee);
    }

    /**
     * Hard delete: Completely removes the employee record from the database.
     */
    @Override
    public void hardDeleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    /**
     * Completely remove all employees from the database.
     */
    @Override
    public long hardDeleteAllEmployees() {
        long deletedCount = employeeRepository.count();
        employeeRepository.deleteAllInBatch();
        return deletedCount;
    }

    /**
     * Search employees by Employee ID or partial/full name.
     * If multiple employees match a partial name (e.g. 'John' -> 'John Doe', 'Johnson'),
     * all matches are returned.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployees(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveEmployees();
        }

        String cleanedKeyword = keyword.trim();
        List<Employee> matches = employeeRepository.searchByKeywordAndStatus(cleanedKeyword, 1);

        return matches.stream()
                .filter(emp -> emp.getStatus() != null && emp.getStatus() == 1)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve inactive / soft-deleted employees (status == 0).
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getInactiveEmployees() {
        return employeeRepository.findByStatus(0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Restore an employee back to active status (status = 1).
     */
    @Override
    public EmployeeDTO restoreEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus(1);
        Employee restored = employeeRepository.save(employee);
        return convertToDTO(restored);
    }

    // Helper method to convert Entity to DTO
    private EmployeeDTO convertToDTO(Employee entity) {
        return new EmployeeDTO(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getName(),
                entity.getTimeIn(),
                entity.getTimeOut(),
                entity.getSalary(),
                entity.getStatus()
        );
    }
}
