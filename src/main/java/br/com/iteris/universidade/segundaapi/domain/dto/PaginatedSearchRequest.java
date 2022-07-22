package br.com.iteris.universidade.segundaapi.domain.dto;
import lombok.Data;
@Data
public class PaginatedSearchRequest {
    private Integer PaginaAtual;
    private Integer QtdPorPagina;
}
