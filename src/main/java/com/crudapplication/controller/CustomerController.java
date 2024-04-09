package com.crudapplication.controller;

import com.crudapplication.entity.Customer;
import com.crudapplication.repository.CustomerRepository;
import com.crudapplication.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id")
                                            Long id) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User not found for this id :: " + id));
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer)  {
      LocalDate today = LocalDate.now();
      customer.setDate(today);
      final Customer updateUser = customerRepository.save(customer);
      return new ResponseEntity<>(updateUser, HttpStatus.CREATED);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id,@RequestBody Customer customerNew)
            throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User not found for this id :: " + id));

        customer.setSum(customerNew.getSum());
        customer.setAdress(customerNew.getAdress());
        customer.setName(customerNew.getName());
        customer.setId(id);
        //long d = System.currentTimeMillis();
        //date = new Date(0);
        //date.setTime(d);
        customer.setDate(customerNew.getDate());
        final Customer updateCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/customer/{id}")
    public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id")
                                           Long id) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User not found for this id :: " + id));

        customerRepository.delete(customer);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
