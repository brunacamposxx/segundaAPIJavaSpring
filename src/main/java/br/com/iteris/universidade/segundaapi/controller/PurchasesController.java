package br.com.iteris.universidade.segundaapi.controller;

import br.com.iteris.universidade.segundaapi.domain.dto.PurchaseCreateRequest;
import br.com.iteris.universidade.segundaapi.domain.dto.PurchaseResponse;
import br.com.iteris.universidade.segundaapi.domain.dto.ResponseBase;
import br.com.iteris.universidade.segundaapi.domain.dto.SearchOrdersRequest;
import br.com.iteris.universidade.segundaapi.service.PurchasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController()
@RequiredArgsConstructor
public class PurchasesController {
    private final PurchasesService purchasesService;

    @PostMapping("api/purchases")
    public ResponseEntity Post(@Valid @RequestBody PurchaseCreateRequest filter) {
        ResponseBase<PurchaseResponse> cadastrarResponse = purchasesService.cadastrar(filter);

        return ResponseEntity.ok(cadastrarResponse);
    }

    /**
     * @return Lista de compras paginada
     */
    @GetMapping("api/purchases")
    public ResponseEntity pesquisar(SearchOrdersRequest searchRequest) {

        ResponseBase<Page<PurchaseResponse>> retorno = purchasesService.pesquisar(searchRequest);

        return ResponseEntity.ok(retorno);
    }
}
