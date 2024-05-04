package com.swiftfingers.mockitotests.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.swiftfingers.mockitotests.entity.Employee;
import com.swiftfingers.mockitotests.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EmployeeController.class)
//@WithMockUser
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService empService;

    @Autowired
    private ObjectMapper objectMapper;
    private final String URL = "/employee";

    @Test
    public void testAddEmployee() throws Exception {

        // prepare data and mock's behaviour
        Employee empStub = new Employee(1l, "bytes", "tree", "developer", 12000);
        when(empService.save(any(Employee.class))).thenReturn(empStub);

        // execute
        ResultActions result = mockMvc.perform(post(URL + "/create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.objectToJson(empStub)));

                 // verify that service method was called once
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(empService).save(captor.capture());


                               // Asserting the response expectations
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", CoreMatchers.is(empStub.getFirstname())))
                .andExpect(jsonPath("$.designation", CoreMatchers.is(empStub.getDesignation())));


        Employee resultEmployee = TestUtils.jsonToObject(result.andReturn().getResponse().getContentAsString(), Employee.class);

        assertNotNull(resultEmployee);
        assertEquals(captor.getValue().getDesignation(), resultEmployee.getDesignation());
        assertEquals(1l, resultEmployee.getId().longValue());

        //verify that there are no more interactions with this mock
        verifyNoMoreInteractions(empService);
    }

    @Test
    public void testGetEmployee() throws Exception {

        // prepare data and mock's behaviour
        Employee empStub = new Employee(1l, "bytes", "tree", "developer", 12000);
        when(empService.getById(any(Long.class))).thenReturn(empStub);

        // execute
//        MvcResult result = mockMvc
//                .perform(get(URL + "/findOne/{id}", Long.valueOf(1))
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andReturn();

        ResultActions response = mockMvc.perform(get(URL + "/findOne/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empStub)));

        // Asserting the response expectations
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", CoreMatchers.is(empStub.getFirstname())))
                .andExpect(jsonPath("$.lastname", CoreMatchers.is(empStub.getLastname())));

        // verify that service method was called once
        verify(empService, times(1)).getById(any(Long.class));

        Employee resultEmployee = TestUtils.jsonToObject(response.andReturn().getResponse().getContentAsString(), Employee.class);
        assertNotNull(resultEmployee);
        assertEquals(1l, resultEmployee.getId().longValue());

        verifyNoMoreInteractions(empService);
    }

    @Test
    public void testGetEmployeeNotExist() throws Exception {

        // prepare data and mock's behaviour
        // Not Required as employee Not Exist scenario

        // execute
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "/findOne/{id}", Long.valueOf(1)).accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

                // verify that service method was called once
        verify(empService).getById(any(Long.class));

        Employee resultEmployee = TestUtils.jsonToObject(result.getResponse().getContentAsString(),
                Employee.class);

        assertNull(resultEmployee);
    }

    @Test
    public void testGetAllEmployee() throws Exception {

        // prepare data and mock's behaviour
        List<Employee> empList = buildEmployees();
        given(empService.getAll()).willReturn(empList);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/findAll")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();


        // verify that service method was called once
        verify(empService).getAll();

        // get the List<Employee> from the Json response
        TypeToken<List<Employee>> token = new TypeToken<List<Employee>>() {
        };
        @SuppressWarnings("unchecked")
        List<Employee> empListResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull(empListResult);
        assertEquals(empList.size(), empListResult.size());

    }

    @Test
    public void testDeleteEmployee() throws Exception {
        // prepare data and mock's behaviour
        Employee empStub = new Employee(1l);
        when(empService.getById(any(Long.class))).thenReturn(empStub);

        // execute
        MvcResult result = mockMvc.perform(delete(URL + "/delete/{id}", Long.valueOf(1))).andReturn();

        // verify that service method was called once
        verify(empService).delete(any(Long.class));

    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // prepare data and mock's behaviour
        // here the stub is the updated employee object with ID equal to ID of
        // employee need to be updated
        Employee empStub = new Employee(1l, "bytes", "tree", "developer", 12000);
        when(empService.getById(any(Long.class))).thenReturn(empStub);

        // execute
        MvcResult result = mockMvc.perform(put(URL + "/update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(TestUtils.objectToJson(empStub))).andReturn();


        // verify that service method was called once
        verify(empService).save(any(Employee.class));

        //verify the order of interactions with the mock
        InOrder inOrder = inOrder(empService);
        inOrder.verify(empService).getById(any(Long.class));
        inOrder.verify(empService).save(any(Employee.class));


    }

    private List<Employee> buildEmployees() {
        Employee e1 = new Employee(1l, "bytes", "tree", "developer", 12000);
        Employee e2 = new Employee(2l, "bytes2", "tree2", "Senior developer", 16000);
        List<Employee> empList = Arrays.asList(e1, e2);
        return empList;
    }


}
