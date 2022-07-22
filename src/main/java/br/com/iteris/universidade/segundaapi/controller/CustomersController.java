package br.com.iteris.universidade.segundaapi.controller;

import br.com.iteris.universidade.segundaapi.domain.dto.CustomerCreateRequest;
import br.com.iteris.universidade.segundaapi.domain.dto.CustomerResponse;
import br.com.iteris.universidade.segundaapi.domain.dto.PaginatedSearchRequest;
import br.com.iteris.universidade.segundaapi.domain.dto.ResponseBase;
import br.com.iteris.universidade.segundaapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequiredArgsConstructor
public class CustomersController {
    private final CustomerService customerService;
    /**
     * @return Lista de customer paginada
     */
    @GetMapping("api/costumers")
    public ResponseEntity pesquisar(PaginatedSearchRequest searchRequest) {
        ResponseBase<Page<CustomerResponse>> retorno = customerService.pesquisar(searchRequest);
        return ResponseEntity.ok(retorno);
    }
    /**
     * @param id Id do customer em questão
     * @return Customer que foi pesquisado
     */
    @GetMapping(value = "api/costumers/{id}")
    public ResponseEntity pesquisaPorId(@PathVariable Long id) {
        ResponseBase<CustomerResponse> retorno = customerService.pesquisaPorId(id);
        return ResponseEntity.ok(retorno);
    }
    /**
     * @param postModel objeto com as informações do novo customer
     * @return Customer que foi criado
     */
    @PostMapping("api/costumers")
    public ResponseEntity cadastrar(@Valid @RequestBody CustomerCreateRequest postModel) {
        ResponseBase<CustomerResponse> retorno = customerService.cadastrar(postModel);
        return ResponseEntity.ok(retorno);
    }
}
