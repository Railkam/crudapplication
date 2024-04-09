package com.crudapplication.conrtoller;

import com.crudapplication.entity.Orders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crudapplication.controller.OrdersController;
import com.crudapplication.entity.Customer;
import com.crudapplication.repository.CustomerRepository;
import com.crudapplication.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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


@WebMvcTest(OrdersController.class)

class OrdersControllerTest {

    @MockBean
    private OrdersRepository ordersRepository;
    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrders () throws Exception{
        List<Orders> orders = new ArrayList<>();
        orders.add(new Orders(1L,"123","Молоко",100000L));

        when(ordersRepository.findAll()).thenReturn(orders);
        mockMvc.perform(get("/customer/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].number").value("123"))
                .andExpect(jsonPath("$.[0].name").value("Молоко"))
                .andExpect(jsonPath("$.[0].quantity").value(100000L));


    }

    @Test
    void getOrderByIdCustomer () throws Exception{
        Date date = new Date(2024);
        Long id=1L;
        List<Orders> orders = new ArrayList<>();
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        orders.add(new Orders(1L,"123","Молоко",100000L));
        orders.add(new Orders(2L,"321","Хлеб",200000L));
        when(customerRepository.findById(id)).thenReturn(Optional.of(cust));
        when(ordersRepository.findByCustomers_Id(id)).thenReturn(orders);
        mockMvc.perform(get("/customer/{id}/orders", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].number").value("123"))
                .andExpect(jsonPath("$.[1].name").value("Хлеб"))
                .andExpect(jsonPath("$.[1].quantity").value(200000L));
    }

    @Test
    void getOrderById () throws Exception{
        Date date = new Date(2024);
        Long id=1L;
        Long orders_id=1L;
        List<Orders> orders = new ArrayList<>();
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        orders.add(new Orders(1L,"123","Молоко",100000L));
        orders.add(new Orders(2L,"321","Хлеб",200000L));
        when(customerRepository.findById(id)).thenReturn(Optional.of(cust));
        when(ordersRepository.findByCustomers_IdAndId(id,orders_id)).thenReturn(orders);
        mockMvc.perform(get("/customer/{id}/orders/{orders_id}", id, orders_id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].number").value("123"))
                .andExpect(jsonPath("$.[1].name").value("Хлеб"))
                .andExpect(jsonPath("$.[1].quantity").value(200000L));
    }

    @Test
    void newOrder () throws Exception{
        Date date = new Date(2024);
        Long id=1L;
        Long orders_id=1L;
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        Orders order =new Orders(1L,"123","Молоко",100000L);
        when(customerRepository.findById(id)).thenReturn(Optional.of(cust));
        order.setCustomer(cust);
        when(ordersRepository.findByCustomers_IdAndId(id,orders_id)).thenReturn(new ArrayList<>());
        when(ordersRepository.save(order)).thenReturn(order);
        mockMvc.perform(post("/customer/{id}/orders", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());


    }

    @Test
    void updateOrder () throws Exception{
        Date date = new Date(2024);
        Long id=1L;
        Long orders_id=1L;
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        Orders order =new Orders(1L,"123","Молоко",100000L);
        Orders orderNew =new Orders(1L,"123","Хлеб",100000L);
        order.setCustomer(cust);
        orderNew.setCustomer(cust);
        when(customerRepository.findById(id)).thenReturn(Optional.of(cust));
        when(ordersRepository.findByIdAndCustomers(id,cust)).thenReturn(Optional.of(order));

        when(ordersRepository.save(any(Orders.class))).thenReturn(orderNew);
        mockMvc.perform(put("/customer/{id}/orders/{orders_id}", id, orders_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNew)))
                .andExpect(jsonPath("$.id").value(orderNew.getId()))
                .andExpect(jsonPath("$.number").value(orderNew.getNumber()))
                .andExpect(jsonPath("$.name").value(orderNew.getName()))
                .andExpect(status().isOk());


    }

    @Test
    void deletOrder () throws Exception{
        Date date = new Date(2024);
        Long id=1L;
        Long orders_id=1L;
        Customer cust = new Customer(1L,"Ильдар","Москва",100000L, date);
        Orders order =new Orders(1L,"123","Молоко",100000L);
        order.setCustomer(cust);
        when(customerRepository.findById(id)).thenReturn(Optional.of(cust));
        when(ordersRepository.findByIdAndCustomers(id,cust)).thenReturn(Optional.of(order));
        doNothing().when(ordersRepository).delete(order);
        mockMvc.perform(delete("/customer/{id}/orders/{orders_id}", id, orders_id))
                        .andExpect(status().isOk());

    }
}
