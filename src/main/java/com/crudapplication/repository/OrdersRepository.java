package com.crudapplication.repository;

import com.crudapplication.entity.Customer;
import com.crudapplication.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {


    List  <Orders> findByCustomers_Id (Long id);
    List  <Orders> findByCustomers_IdAndId (Long id, Long orders_id);
    Optional<Orders> findByIdAndCustomers (Long Id, Customer customers);
}