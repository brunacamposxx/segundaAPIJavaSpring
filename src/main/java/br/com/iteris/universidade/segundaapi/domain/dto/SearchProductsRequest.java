package br.com.iteris.universidade.segundaapi.domain.dto;

import lombok.Data;

@Data
public class SearchProductsRequest extends PaginatedSearchRequest {
    private Boolean IsDiscontinued;
}
