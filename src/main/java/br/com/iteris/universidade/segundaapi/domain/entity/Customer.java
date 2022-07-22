package br.com.iteris.universidade.segundaapi.domain.entity;

import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "FirstName", length = 40, nullable = false)
    private String firstName;
    @Column(name = "LastName", length = 40, nullable = false)
    private String lastName;
    @Column(name = "City", length = 40)
    private String city;
    @Column(name = "Country", length = 40)
    private String country;
    @Column(name = "Phone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "customerId", fetch = FetchType.LAZY)
    private List<Order> orders;
}