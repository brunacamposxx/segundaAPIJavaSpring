package br.com.iteris.universidade.segundaapi.domain.dto;

import br.com.iteris.universidade.segundaapi.domain.entity.Product;
import lombok.Data;

@Data
public class ProductResponse {

    private long Id;
    private String ProductName;
    private Double UnitPrice;
    private boolean IsDiscontinued;

    public ProductResponse(Product product) {
        Id = product.getId();
        ProductName = product.getProductName();
        UnitPrice = product.getUnitPrice();
        IsDiscontinued = product.getIsDiscontinued();
    }
}
