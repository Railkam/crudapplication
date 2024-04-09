package com.crudapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "orders")
@IdClass(OrderIdCustomerId.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orders {
   @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "num_product")
    private String number;
    @Column(name = "name_product")
    private String name;
    @Column(name = "quantity_product")
    private long quantity;
    @ManyToOne(
            fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customerid", nullable = false)
    //@JsonIgnore
    private Customer customers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders order = (Orders) o;
        return id.equals(order.id) &&
                customers.equals(order.customers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customers);
    }

    public Orders() {
    }

    public Orders(Long id, String number, String name, Long quantity) {
        //super();
        this.id = id;
        this.number = number;
        this.name = name;
        this.quantity = quantity;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    //@JsonIgnore
    public Customer getCustomer() {
        return customers;
    }

    public void setCustomer(Customer customers) {
        this.customers = customers;
    }

}
