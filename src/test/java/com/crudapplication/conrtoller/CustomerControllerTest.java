package com.crudapplication.conrtoller;

import com.crudapplication.controller.CustomerController;
import com.crudapplication.entity.Customer;
import com.crudapplication.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)

class CustomerControllerTest {

    @MockBean
    private CustomerController customerController;
    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void AllCustomers () throws Exception{
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L,"Ильдар","Москва",100000L, new Date(0)));

        when(customerController.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(get("/customer"))
        .andExpect(status().isOk());

    }

    @Test
    void getCustomerById () throws Exception{
        Long id = 1L;
        Date date = new Date(2024);
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        Mockito.doReturn(new ResponseEntity<>(cust, HttpStatus.OK)).when(customerController).getCustomerById(1L);
        mockMvc.perform(get("/customer/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ильдар"))
                .andExpect(jsonPath("$.adress").value("Москва"))
                .andExpect(jsonPath("$.sum").value(100000L));
    }

    @Test
    void creatCustomer () throws Exception{
        Date date = new Date(2024);
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cust)))
                .andExpect(status().isOk());
    }


    @Test
    public void givenUpdateCustomer() throws Exception{
        // given - precondition or setup
        Long customerId = 1L;
        Date date = new Date(2024);
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        Customer cust2 = new Customer(1L,"Ильдар2","Москва2",100000L, date);
        when(customerRepository.findById(customerId )).thenReturn(Optional.of(cust));
        when(customerRepository.save(any(Customer.class))).thenReturn(cust2);

//        Mockito.doReturn(new ResponseEntity<>(cust, HttpStatus.OK)).when(customerController).getUserById(customerId);
//        given(customerController.updateUser(customerId,cust2)).willReturn(new ResponseEntity<>(cust2, HttpStatus.OK));
       mockMvc.perform(put("/customer/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cust2)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCustomer() throws Exception {
        Long id = 1L;
        Date date = new Date(2024);
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        doNothing().when(customerRepository).delete(cust);
        mockMvc.perform(delete("/customer/{id}", id))
                .andExpect(status().isOk());
    }

}
