package br.com.iteris.universidade.segundaapi.domain.dto;

import br.com.iteris.universidade.segundaapi.domain.entity.Order;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PurchaseResponse {
    private Long id;
    private LocalDateTime orderDate;
    private double totalAmount;

    public PurchaseResponse(Order novaOrder) {
        id = novaOrder.getId();
        orderDate = novaOrder.getOrderDate();
        totalAmount = novaOrder.getTotalAmount();
    }
}
