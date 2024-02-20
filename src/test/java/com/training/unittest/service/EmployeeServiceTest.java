package com.training.unittest.service;

import com.training.unittest.exception.EmployeeExceptions;
import com.training.unittest.model.Employee;
import com.training.unittest.repository.EmployeeRepo;
import com.training.unittest.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepo employeeRepo;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    public void setup() {
        //employeeRepo = Mockito.mock(EmployeeRepo.class);
        //employeeService = new EmployeeServiceImpl(employeeRepo);
        employee = Employee.builder()
                .id(1L)
                .firstName("Mj")
                .lastName("Farzaneh")
                .email("mjfarzaneh7@gmail.com")
                .build();
    }

    @DisplayName("saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given
        //these two lines are the stubbing methods that used in employeeService.saveEmployee() method.
        given(employeeRepo.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepo.save(employee)).willReturn(employee);

        System.out.println(employeeRepo);
        System.out.println(employeeService);
        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepo.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //given(employeeRepo.save(employee)).willReturn(employee);

        System.out.println(employeeRepo);
        System.out.println(employeeService);
        //when
        Assertions.assertThrows(EmployeeExceptions.class, () ->
                employeeService.saveEmployee(employee));

        verify(employeeRepo, never()).save(any(Employee.class));
    }

    @DisplayName("getAllEmployees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_returnReturnEmployeeList() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Niusha")
                .lastName("Tahmasebi")
                .email("niushatt@gmail.com")
                .build();
        given(employeeRepo.findAll()).willReturn(List.of(employee, employee1));

        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_returnReturnEmptyEmployeeList() {

        given(employeeRepo.findAll()).willReturn(Collections.emptyList());

        //when
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("getEmployeeId method")
    @Test
    public void givenEmployeeId_whenFindById_returnEmployeeObject() {
        given(employeeRepo.findById(1L)).willReturn(Optional.of(employee));

        Optional<Employee> employeeById = employeeService.getEmployeeById(employee.getId());

        assertThat(employeeById.get()).isNotNull();
        assertThat(employeeById.get().getEmail()).isEqualTo("mjfarzaneh7@gmail.com");
    }

    @DisplayName("updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_returnUpdatedEmployeeObject() {
        given(employeeRepo.save(employee)).willReturn(employee);
        employee.setEmail("m.javad2007@gmail.com");
        employee.setFirstName("Mohammad Javad");

        Employee updatedEmployee = employeeService.updateEmployee(employee);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Mohammad Javad");
        assertThat(updatedEmployee.getEmail()).isEqualTo("m.javad2007@gmail.com");
    }

    @DisplayName("deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_returnNothing() {
        willDoNothing().given(employeeRepo).deleteById(employee.getId());

        employeeService.deleteEmployee(employee.getId());

        verify(employeeRepo, times(1)).deleteById(employee.getId());
    }
}
