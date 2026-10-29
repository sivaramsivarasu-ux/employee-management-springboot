package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

    @NotBlank(message = "Employee name cannot be blank")
    private String name;

    @NotBlank(message = "Time in cannot be blank (e.g. 09:00 AM)")
    private String timeIn;

    @NotBlank(message = "Time out cannot be blank (e.g. 05:00 PM)")
    private String timeOut;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive number")
    private Double salary;

    private Integer status = 1;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String employeeId, String name, String timeIn, String timeOut, Double salary, Integer status) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.salary = salary;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
