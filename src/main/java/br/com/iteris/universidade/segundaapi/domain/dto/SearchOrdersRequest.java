package br.com.iteris.universidade.segundaapi.domain.dto;

import lombok.Data;

@Data
public class SearchOrdersRequest extends PaginatedSearchRequest {
    private Double minPrice;
}
