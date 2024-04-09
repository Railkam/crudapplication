package com.crudapplication.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "customer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_cust")
    private String name;
    @Column(name = "addres_cust")
    private String adress;
    @Column(name = "summ")
    private long sum;
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private LocalDate date;


    @OneToMany(mappedBy = "customers",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)

    private Set<Orders> orders;

    public Customer() {
    }

    public Customer(Long id, String name, String adress, Long sum, LocalDate date) {
         super();
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.sum = sum;
        this.date = date;
    }

    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    @JsonIgnore
    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }


}
