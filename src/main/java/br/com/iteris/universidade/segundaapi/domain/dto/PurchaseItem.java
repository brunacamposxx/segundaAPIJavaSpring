package br.com.iteris.universidade.segundaapi.domain.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PurchaseItem {
    @NotEmpty
    private Long ProductId;
    @NotEmpty
    private Double Discount;
    @NotEmpty
    private Integer Quantity;
}
