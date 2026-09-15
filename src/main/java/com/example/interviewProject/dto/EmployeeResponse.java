package com.example.interviewProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmployeeResponse {

    @Schema(
            description = "Unique employee identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Employee's first name",
            example = "Dilip"
    )
    private String firstName;

    @Schema(
            description = "Employee's first name",
            example = "Dilip"
    )
    private String lastName;

    @Schema(
            description = "Employee's email address",
            example = "dilip@example.com"
    )
    private String email;

    @Schema(
            description = "Employee's department",
            example = "IT"
    )
    private String department;

    @Schema(
            description = "Employee's annual salary",
            example = "75000"
    )
    private Double salary;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id,
                            String firstName,
                            String lastName,
                            String email,
                            String department,
                            Double salary) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public Double getSalary() {
        return salary;
    }
}