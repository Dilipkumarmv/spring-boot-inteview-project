package com.example.interviewProject.controller;

import com.example.interviewProject.dto.EmployeeResponse;
import com.example.interviewProject.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.interviewProject.dto.EmployeeRequest;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;
import com.example.interviewProject.exception.EmployeeNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldGetEmployeeById() throws Exception {

        // Arrange
        EmployeeResponse response = new EmployeeResponse(
                1L,
                "Dilip",
                "Kumar",
                "dilip@example.com",
                "IT",
                75000.0
        );

        when(employeeService.getEmployeeById(1L))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Dilip"))
                .andExpect(jsonPath("$.lastName").value("Kumar"))
                .andExpect(jsonPath("$.email").value("dilip@example.com"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(75000.0));
    }

    @Test
    void shouldCreateEmployee() throws Exception {

        // Arrange
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Rahul");
        request.setLastName("Sharma");
        request.setEmail("rahul@example.com");
        request.setDepartment("Engineering");
        request.setSalary(85000.0);

        EmployeeResponse response = new EmployeeResponse(
                3L,
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "Engineering",
                85000.0
        );

        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "firstName": "Rahul",
                                            "lastName": "Sharma",
                                            "email": "rahul@example.com",
                                            "department": "Engineering",
                                            "salary": 85000
                                        }
                                        """
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value("Rahul"))
                .andExpect(jsonPath("$.lastName").value("Sharma"))
                .andExpect(jsonPath("$.email").value("rahul@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(85000.0));
    }

    @Test
    void shouldUpdateEmployee() throws Exception {

        // Arrange
        EmployeeResponse response = new EmployeeResponse(
                1L,
                "Dilip",
                "Kumar",
                "dilip.updated@example.com",
                "Architecture",
                95000.0
        );

        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class)
        )).thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "firstName": "Dilip",
                                            "lastName": "Kumar",
                                            "email": "dilip.updated@example.com",
                                            "department": "Architecture",
                                            "salary": 95000
                                        }
                                        """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Dilip"))
                .andExpect(jsonPath("$.email")
                        .value("dilip.updated@example.com"))
                .andExpect(jsonPath("$.department")
                        .value("Architecture"))
                .andExpect(jsonPath("$.salary").value(95000.0));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {

        // Arrange
        doNothing().when(employeeService).deleteEmployee(1L);

        // Act + Assert
        mockMvc.perform(
                        delete("/api/v1/employees/1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllEmployees() throws Exception {

        // Arrange
        EmployeeResponse employee1 = new EmployeeResponse(
                1L,
                "Dilip",
                "Kumar",
                "dilip@example.com",
                "IT",
                75000.0
        );

        EmployeeResponse employee2 = new EmployeeResponse(
                2L,
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "Engineering",
                85000.0
        );

        when(employeeService.getAllEmployees())
                .thenReturn(List.of(employee1, employee2));

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Dilip"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Rahul"));
    }

    @Test
    void shouldReturn404WhenEmployeeDoesNotExist() throws Exception {

        // Arrange
        when(employeeService.getEmployeeById(999L))
                .thenThrow(
                        new EmployeeNotFoundException(
                                "Employee not found with id: 999"
                        )
                );

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingEmployee() throws Exception {

        // Arrange
        doThrow(
                new EmployeeNotFoundException(
                        "Employee not found with id: 999"
                )
        ).when(employeeService).deleteEmployee(999L);

        // Act + Assert
        mockMvc.perform(
                        delete("/api/v1/employees/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn400ForInvalidEmployeeRequest() throws Exception {

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "firstName": "",
                                            "lastName": "",
                                            "email": "invalid-email",
                                            "department": "",
                                            "salary": -100
                                        }
                                        """
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.department").exists())
                .andExpect(jsonPath("$.salary").exists());
    }

    @Test
    void shouldReturn400ForInvalidUpdateRequest() throws Exception {

        mockMvc.perform(
                        put("/api/v1/employees/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                            "firstName": "",
                                            "lastName": "",
                                            "email": "wrong-email",
                                            "department": "",
                                            "salary": -500
                                        }
                                        """
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.department").exists())
                .andExpect(jsonPath("$.salary").exists());
    }
}