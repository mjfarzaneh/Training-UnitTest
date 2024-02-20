package com.training.unittest.repository;

import com.training.unittest.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//This annotation only load the repository layer of application.
//In other word, it doesn't load other layers like controller or service layer.
public class EmployeeRepoTests {
    private final EmployeeRepo employeeRepo;
    private Employee mj;

    @Autowired
    public EmployeeRepoTests(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @BeforeEach
    public void setup() {
        mj =
                Employee.builder()
                        .firstName("Mj")
                        .lastName("Farzaneh")
                        .email("mjfarzaneh7@gmail.com")
                        .build();
    }

    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup

        Employee niu =
                Employee.builder()
                        .firstName("Niusha")
                        .lastName("Tahmasebi")
                        .email("niushatt@gmail.com")
                        .build();

        //when - action or the behaviour that we are going to test
        Employee savedMj = employeeRepo.save(mj);
        Employee savedNiu = employeeRepo.save(niu);
        //then - verify the output
        assertThat(savedMj).isNotNull();
        assertThat(savedMj.getId()).isGreaterThan(0);
        assertThat(savedMj.getId()).isLessThan(savedNiu.getId());
        assertThat(savedMj.getEmail()).isNotNull();

    }

    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeesList() {
        //given
        Employee niu =
                Employee.builder()
                        .firstName("Niusha")
                        .lastName("Tahmasebi")
                        .email("niushatt@gmail.com")
                        .build();

        employeeRepo.save(mj);
        employeeRepo.save(niu);

        //when
        List<Employee> employeeList = employeeRepo.findAll();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get employee by ID operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeById() {
        //given

        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findById(mj.getId()).get();

        //then
        assertThat(findMj).isNotNull();

    }

    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeById() {
        //given
        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findByEmail(mj.getEmail()).get();

        //then
        assertThat(findMj).isNotNull();

    }

    @DisplayName("JUnit test for get update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateFirstNameAndEmail_thenReturnEmployeeById() {
        //given
        employeeRepo.save(mj);


        //when
        Employee findMj = employeeRepo.findByEmail(mj.getEmail()).get();
        findMj.setEmail("mjfarzaneh7@gmail.com");
        findMj.setFirstName("Mohammad Javad");

        Employee updatedMj = employeeRepo.save(findMj);

        //then
        assertThat(updatedMj.getFirstName()).isEqualTo("Mohammad Javad");
        assertThat(updatedMj.getEmail()).isEqualTo("mjfarzaneh7@gmail.com");

    }

    @Test
    public void givenEmployeeObject_whenDeleteById_thenReturnNull() {
        //given
        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findByEmail("mjfarzaneh7@gmail.com").get();
        employeeRepo.deleteById(findMj.getId());

        //then
        boolean isMjExist = employeeRepo.findById(findMj.getId()).isPresent();
        assertThat(isMjExist).isFalse();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenEmployeeObject() {
        //given

        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findByJPQL("Mj", "Farzaneh");

        //then
        assertThat(findMj).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNameParams_thenEmployeeObject() {
        //given

        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findByJPQLNameParams("Mj", "Farzaneh");

        //then
        assertThat(findMj).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenEmployeeObject() {
        //given

        employeeRepo.save(mj);

        //when
        Employee findMj = employeeRepo.findByNativeSQL("Mj", "Farzaneh");

        //then
        assertThat(findMj).isNotNull();
    }

}
