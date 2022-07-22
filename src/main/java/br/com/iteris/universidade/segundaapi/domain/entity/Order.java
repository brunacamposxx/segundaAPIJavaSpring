package br.com.iteris.universidade.segundaapi.domain.entity;

import java.time.LocalDateTime;
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
@Table(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "OrderDate", columnDefinition = "datetime")
    private LocalDateTime orderDate;
    @Column(name = "OrderNumber", length = 10)
    private String orderNumber;
    @Column(name = "TotalAmount", columnDefinition = "decimal(12, 2)")
    private double totalAmount;

    @Column(name = "CustomerId", insertable = false, updatable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}