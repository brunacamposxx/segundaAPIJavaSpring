package br.com.iteris.universidade.segundaapi.service;
import br.com.iteris.universidade.segundaapi.domain.dto.CustomerCreateRequest;
import br.com.iteris.universidade.segundaapi.domain.dto.CustomerResponse;
import br.com.iteris.universidade.segundaapi.domain.dto.PaginatedSearchRequest;
import br.com.iteris.universidade.segundaapi.domain.dto.ResponseBase;
import br.com.iteris.universidade.segundaapi.domain.entity.Customer;
import br.com.iteris.universidade.segundaapi.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CustomerService {
    // Usa o lombok para injetar o repositorio (https://www.baeldung.com/spring-injection-lombok)
    private final CustomersRepository customersRepository;
    public ResponseBase<CustomerResponse> pesquisaPorId(long id) {
        // Consulta o repositorio para procurar por um custumer pelo id
        Optional<Customer> customerOptional = customersRepository.findById(id);
        // Verifica se o custimer foi encontrado, caso o contratrio retorna um erro
        Customer customer = customerOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objeto Não encontrado"));
        // Mapeia de entidade para dto
        CustomerResponse customerResponse = new CustomerResponse(customer);
        return new ResponseBase<>(customerResponse);
    }
    public ResponseBase<CustomerResponse> cadastrar(CustomerCreateRequest novo) {
        // Cria uma entidade a partir do DTO
        Customer modeloDb = new Customer();
        modeloDb.setFirstName(novo.getFirstName());
        modeloDb.setLastName(novo.getLastName());
        modeloDb.setCity(novo.getCity());
        modeloDb.setPhone(novo.getPhone());
        // Usa o repository para salvar o novo customer
        Customer customerSalvo = customersRepository.save(modeloDb);
        // Mapeia de entidade para dto
        CustomerResponse customerResponse = new CustomerResponse(customerSalvo);
        return new ResponseBase<>(customerResponse);
    }
    public ResponseBase<Page<CustomerResponse>> pesquisar(PaginatedSearchRequest searchRequest) {
        // a Pagina atual não pode ser menor que 0
        if (searchRequest.getPaginaAtual() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O indice da página atual deve começar em 0");
        }
        // a quantidade de itens por pagina deve ser entre 1 e 50
        if (searchRequest.getQtdPorPagina() < 1 || searchRequest.getQtdPorPagina() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de itens por página deve ser entre 1 e 50 itens");
        }
        // cria um objeto de consulta paginada(PageRequest) a partir dos parametros informados
        PageRequest pageRequest = PageRequest.of(searchRequest.getPaginaAtual(), searchRequest.getQtdPorPagina());
        // pesquisa no repositorio de customer usando a consulta paginada
        Page<Customer> customerPage = customersRepository.findAll(pageRequest);
        // Mapeia da entidade(Customer) para o DTO(CustomerResponse)
        Page<CustomerResponse> customerResponsePage = customerPage.map(CustomerResponse::new);
        return new ResponseBase<>(customerResponsePage);
    }
}
