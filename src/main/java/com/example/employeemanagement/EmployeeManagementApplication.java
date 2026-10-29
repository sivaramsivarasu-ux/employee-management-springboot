package com.example.employeemanagement;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    /**
     * Seeds initial sample employees if database is empty.
     * Demonstrates partial name search (e.g. searching 'John' returns John Doe, Jonathan Smith, Alice Johnson).
     */
    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(Arrays.asList(
                    new Employee("EMP101", "John Doe", "09:00 AM", "05:00 PM", 65000.0),
                    new Employee("EMP102", "Jonathan Smith", "08:30 AM", "04:30 PM", 72000.0),
                    new Employee("EMP103", "Alice Johnson", "09:15 AM", "05:15 PM", 58000.0),
                    new Employee("EMP104", "Robert Johnson", "10:00 AM", "06:00 PM", 80000.0),
                    new Employee("EMP105", "Sarah Williams", "09:00 AM", "05:00 PM", 67000.0),
                    new Employee("EMP106", "Michael Brown", "08:00 AM", "04:00 PM", 62000.0),
                    new Employee("EMP107", "Emily Davis", "09:30 AM", "05:30 PM", 75000.0)
                ));
                System.out.println(">> Initial sample employees successfully seeded into MySQL database.");
            }
        };
    }
}
