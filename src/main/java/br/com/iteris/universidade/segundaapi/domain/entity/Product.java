package br.com.iteris.universidade.segundaapi.domain.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ProductName", length = 590, nullable = false)
    private String productName;
    @Column(name = "UnitPrice", columnDefinition = "decimal(12, 2)")
    private double unitPrice;
    @Column(name = "package", length = 30)
    private String packageName;
    @Column(name = "IsDiscontinued", length = 30)
    private Boolean isDiscontinued;

    @Column(name = "SupplierId", insertable = false, updatable = false)
    private Integer supplierId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierId")
    private Supplier supplier;

    @OneToMany(mappedBy = "productId", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}