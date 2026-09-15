package com.example.interviewProject.service;

import com.example.interviewProject.dto.EmployeeRequest;
import com.example.interviewProject.dto.EmployeeResponse;
import com.example.interviewProject.entity.Employee;
import com.example.interviewProject.exception.EmployeeNotFoundException;
import com.example.interviewProject.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldGetEmployeeById() {

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("Dilip");
        employee.setLastName("Kumar");
        employee.setEmail("dilip@example.com");
        employee.setDepartment("IT");
        employee.setSalary(75000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response =
                employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Dilip", response.getFirstName());
        assertEquals("Kumar", response.getLastName());

        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(999L)
        );

        verify(employeeRepository).findById(999L);
    }

    @Test
    void shouldCreateEmployee() {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Rahul");
        request.setLastName("Sharma");
        request.setEmail("rahul@example.com");
        request.setDepartment("Engineering");
        request.setSalary(85000.0);

        Employee savedEmployee = new Employee();

        savedEmployee.setId(3L);
        savedEmployee.setFirstName("Rahul");
        savedEmployee.setLastName("Sharma");
        savedEmployee.setEmail("rahul@example.com");
        savedEmployee.setDepartment("Engineering");
        savedEmployee.setSalary(85000.0);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response =
                employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("Rahul", response.getFirstName());
        assertEquals("Engineering", response.getDepartment());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void shouldUpdateEmployee() {

        Employee existingEmployee = new Employee();

        existingEmployee.setId(1L);
        existingEmployee.setFirstName("Dilip");
        existingEmployee.setLastName("Kumar");
        existingEmployee.setEmail("dilip@example.com");
        existingEmployee.setDepartment("IT");
        existingEmployee.setSalary(75000.0);

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Dilip");
        request.setLastName("Kumar");
        request.setEmail("dilip.updated@example.com");
        request.setDepartment("Architecture");
        request.setSalary(95000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(existingEmployee);

        EmployeeResponse response =
                employeeService.updateEmployee(1L, request);

        assertEquals(1L, response.getId());
        assertEquals(
                "dilip.updated@example.com",
                response.getEmail()
        );
        assertEquals("Architecture", response.getDepartment());
        assertEquals(95000.0, response.getSalary());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void shouldDeleteEmployee() {

        Employee employee = new Employee();

        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(999L)
        );

        verify(employeeRepository).findById(999L);
        verify(employeeRepository, never()).delete(any());
    }
}