package com.training.unittest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.unittest.model.Employee;
import com.training.unittest.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper mapper;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Mj")
                .lastName("Farzaneh")
                .email("mjfarzaneh7@gmail.com")
                .build();
    }

    @DisplayName("createEmployee RestAPI")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }

    @DisplayName("getAllEmployees RestAPI")
    @Test
    public void givenListOfEmployees_whenGetAlEmployees_thenReturnEmployeeList() throws Exception {
        Employee niu = Employee.builder()
                .id(2L)
                .firstName("Niusha")
                .lastName("Tahmasebi")
                .email("niushatt@gmail.com")
                .build();
        given(employeeService.getAllEmployees()).willReturn(List.of(employee, niu));

        ResultActions response = mockMvc.perform(get("/api/employee"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));

    }

    @DisplayName("getEmployeeById RestAPI(positive scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employee.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }
    @DisplayName("getEmployeeById RestAPI(negative scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_then () throws Exception {
        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employee/{id}", 2));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("updateEmployee RestAPI (positive scenario)")
    @Test
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject () throws Exception {

        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        employee.setFirstName("Mohammad Javad");
        employee.setEmail("m.javad2007@gmail.com");
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0 ));

        ResultActions response = mockMvc.perform(post("/api/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }
    @DisplayName("updateEmployee RestAPI (negative scenario)")
    @Test
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnNotFound () throws Exception {

        given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        employee.setFirstName("Mohammad Javad");
        employee.setEmail("m.javad2007@gmail.com");
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0 ));

        ResultActions response = mockMvc.perform(put("/api/employee/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("deleteEmployee RestAPI")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOK () throws Exception {

        willDoNothing().given(employeeService).deleteEmployee(employee.getId());

        ResultActions response = mockMvc.perform(delete("/api/employee/{id}", 1));

        response.andDo(print())
                .andExpect(status().isOk());
    }
}
