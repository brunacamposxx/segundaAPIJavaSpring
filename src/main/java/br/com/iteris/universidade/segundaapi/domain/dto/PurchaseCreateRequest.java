package br.com.iteris.universidade.segundaapi.domain.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseCreateRequest {
    @NotNull
    private Long CustomerId;
    @NotEmpty
    private List<PurchaseItem> Itens;

}
