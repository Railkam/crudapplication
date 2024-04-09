package com.crudapplication.controller;

import com.crudapplication.entity.Customer;
import com.crudapplication.entity.Orders;
import com.crudapplication.exception.ResourceNotFoundException;
import com.crudapplication.repository.CustomerRepository;
import com.crudapplication.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class OrdersController {
    @Autowired
    private CustomerRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    private ResourceNotFoundException resourceNotFoundException;

    @GetMapping("/customer/orders")
    public List <Orders> getOrders() throws ResourceNotFoundException {

        return ordersRepository.findAll();
    }

    @GetMapping("/customer/{id}/orders")
    public List <Orders> getOrderByIdCustomer(@PathVariable Long id) throws ResourceNotFoundException {

       Customer customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Customer ID "+id+" not found"));
        return ordersRepository.findByCustomers_Id(id);
    }
    @GetMapping("/customer/{id}/orders/{orders_id}")
    public List <Orders> getOrderById(@PathVariable Long id,
                                      @PathVariable Long orders_id) throws ResourceNotFoundException {

        Customer customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Customer ID "+id+" not found"));
        if(ordersRepository.findByCustomers_IdAndId(id,orders_id).isEmpty()) {
        throw new ResourceNotFoundException("The order with this ID "+orders_id+" not found");
        }
        return ordersRepository.findByCustomers_IdAndId(id,orders_id);
    }

    @PostMapping("/customer/{id}/orders")
    public Orders postOrder(@PathVariable Long id, @Validated @RequestBody Orders order)
            throws ResourceNotFoundException {
        Customer customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Customer ID "+id+" not found"));
        order.setCustomer(customer);
        if (order.getId()==null) {
            throw new ResourceNotFoundException("Enter the item number");}
        Optional <Orders>  or = ordersRepository.findByIdAndCustomers(order.getId(),customer);
       //  Orders or1 = or.get();
       // or1.equals(order);
        if (!or.isEmpty()) {
            throw new ResourceNotFoundException("The order with this ID "+order.getId()+" already exists");}
            return ordersRepository.save(order);
    }

    @PutMapping("/customer/{id}/orders/{orders_id}")
    public ResponseEntity<Orders> updateOrders(@PathVariable Long id,
                                               @PathVariable Long orders_id,
                                               @RequestBody Orders ordersUpdate)
            throws ResourceNotFoundException {
        Customer customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Customer ID "+id+" not found"));
        Optional <Orders>  or = ordersRepository.findByIdAndCustomers(orders_id,customer);
        if (or.isEmpty()) {
            throw new ResourceNotFoundException("The order with this ID "+orders_id+" not found");}
        if (!orders_id.equals(ordersUpdate.getId()) && ordersUpdate.getId()!=null) {
            throw new ResourceNotFoundException("The unique identifier cannot be changed. Create a new ID "+ordersUpdate.getId());
        }
        Orders order = or.get();
        order.setName(ordersUpdate.getName());
        order.setNumber(ordersUpdate.getNumber());
        order.setQuantity(ordersUpdate.getQuantity());
        final Orders updateOrder = ordersRepository.save(order);
        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("/customer/{id}/orders/{orders_id}")
    public String deleteOrder(@PathVariable Long id,
                                               @PathVariable Long orders_id)
            throws ResourceNotFoundException {
        Customer customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Customer ID "+id+" not found"));
        Optional <Orders>  or = ordersRepository.findByIdAndCustomers(orders_id,customer);
        if (or.isEmpty()) {
            throw new ResourceNotFoundException("The order with this ID "+orders_id+" not found");}
        ordersRepository.delete(or.get());
        return "Deleted Success";
    }
}
