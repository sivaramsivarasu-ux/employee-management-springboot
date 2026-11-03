package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*") // Allows React frontend requests
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * SHOW: Retrieve all active employees (where status == 1).
     * Endpoint: GET /api/employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllActiveEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllActiveEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * SHOW SINGLE: Get employee by primary key ID.
     * Endpoint: GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * NEW: Register a new employee with employeeID, name, timein, timeout, salary.
     * Endpoint: POST /api/employees
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee created successfully");
        response.put("employee", created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * EDIT: Update employee name or information.
     * Endpoint: PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee updated successfully");
        response.put("employee", updated);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE: Delete employee from the list.
     * Endpoint: DELETE /api/employees/{id}?permanent=false
     * By default, performs soft-delete (sets status to 0).
     * If ?permanent=true, performs permanent hard delete.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(
            @PathVariable Long id,
            @RequestParam(value = "permanent", defaultValue = "false") boolean permanent) {
        Map<String, String> response = new HashMap<>();
        if (permanent) {
            employeeService.hardDeleteEmployee(id);
            response.put("message", "Employee permanently deleted from database.");
            response.put("mode", "permanent");
        } else {
            employeeService.softDeleteEmployee(id);
            response.put("message", "Employee marked as inactive (status = 0). Excluded from active employee list.");
            response.put("mode", "soft");
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Permanently delete all employees from the database.
     * Endpoint: DELETE /api/employees
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAllEmployees() {
        long deletedCount = employeeService.hardDeleteAllEmployees();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All employees permanently deleted from database.");
        response.put("deletedCount", deletedCount);
        return ResponseEntity.ok(response);
    }

    /**
     * SEARCH: Search employees by employeeID or partial / full name.
     * If partial name matches multiple employees, returns all of them.
     * Endpoint: GET /api/employees/search?query=john
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(
            @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        List<EmployeeDTO> results = employeeService.searchEmployees(query);
        return ResponseEntity.ok(results);
    }

    /**
     * SHOW INACTIVE: Show employees marked as 0 (recycle bin).
     * Endpoint: GET /api/employees/inactive
     */
    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeDTO>> getInactiveEmployees() {
        List<EmployeeDTO> inactive = employeeService.getInactiveEmployees();
        return ResponseEntity.ok(inactive);
    }

    /**
     * RESTORE: Restore a soft-deleted employee (status back to 1).
     * Endpoint: POST /api/employees/{id}/restore
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreEmployee(@PathVariable Long id) {
        EmployeeDTO restored = employeeService.restoreEmployee(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee restored to active status successfully.");
        response.put("employee", restored);
        return ResponseEntity.ok(response);
    }
}
