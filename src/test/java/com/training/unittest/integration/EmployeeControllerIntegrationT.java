package com.training.unittest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.unittest.model.Employee;
import com.training.unittest.repository.EmployeeRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerIntegrationT extends AbstractionBaseTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;

    @BeforeEach
    void setup() {
        employeeRepo.deleteAll();
        employee = Employee.builder()
                .firstName("Mj")
                .lastName("Farzaneh")
                .email("mjfarzaneh7@gmail.com")
                .build();
    }

    @DisplayName("createEmployee integration")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_returnEmployeeObject() throws Exception {
        //given wrote in @BeforeEach method

        //when
        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("getAllEmployees integration")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        Employee niu = Employee.builder()
                .firstName("Niusha")
                .lastName("Tahmasebi")
                .email("niushatt@gmail.com")
                .build();
        employeeRepo.save(employee);
        employeeRepo.save(niu);

        //when
        ResultActions response = mockMvc.perform(get("/api/employee"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.equalTo(2)));
    }

    @DisplayName("getEmployeeById integration (positive scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        employeeRepo.save(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employee.getId()));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }

    @DisplayName("getEmployeeById integration(negative scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        employeeRepo.save(employee);

        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employee.getId() + 1));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("updateEmployee integration (positive scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        employeeRepo.save(employee);

        employee.setEmail("m.javad2007@gmail.com");
        employee.setFirstName("Mohammad Javad");

        ResultActions response = mockMvc.perform(put("/api/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("updateEmployee integration (negative scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        employeeRepo.save(employee);

        employee.setEmail("m.javad2007@gmail.com");
        employee.setFirstName("Mohammad Javad");

        ResultActions response = mockMvc.perform(put("/api/employee/{id}", employee.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("deleteEmployee integration")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOK() throws Exception {
        employeeRepo.save(employee);

        ResultActions response = mockMvc.perform(delete("/api/employee/{id}", employee.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }
}
