package com.example.interviewProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EmployeeRequest {

    @Schema(
            description = "Employee's first name",
            example = "Dilip"
    )
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(
            description = "Employee's last name",
            example = "kumar"
    )
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(
            description = "Employee's email address",
            example = "dilip@example.com"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(
            description = "Employee's department",
            example = "IT"
    )
    @NotBlank(message = "Department is required")
    private String department;

    @Schema(
            description = "Employee's annual salary",
            example = "75000"
    )
    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    private Double salary;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}