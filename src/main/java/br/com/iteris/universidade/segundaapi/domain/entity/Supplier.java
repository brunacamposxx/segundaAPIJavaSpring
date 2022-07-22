package br.com.iteris.universidade.segundaapi.domain.entity;

import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "CompanyName", length = 40, nullable = false)
    private String companyName;
    @Column(name = "ContactName", length = 50)
    private String contactName;
    @Column(name = "ContactTitle", length = 40)
    private String contactTitle;
    @Column(name = "City", length = 40)
    private String city;
    @Column(name = "Country", length = 40)
    private String country;
    @Column(name = "Phone", length = 30)
    private String phone;
    @Column(name = "Fax", length = 30)
    private String fax;

    @OneToMany(mappedBy = "supplierId", fetch = FetchType.LAZY)
    private List<Product> products;
}
