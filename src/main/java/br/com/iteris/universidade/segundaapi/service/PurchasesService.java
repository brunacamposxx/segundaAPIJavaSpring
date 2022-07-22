package br.com.iteris.universidade.segundaapi.service;

import br.com.iteris.universidade.segundaapi.domain.dto.*;
import br.com.iteris.universidade.segundaapi.domain.entity.Customer;
import br.com.iteris.universidade.segundaapi.domain.entity.Order;
import br.com.iteris.universidade.segundaapi.domain.entity.OrderItem;
import br.com.iteris.universidade.segundaapi.domain.entity.Product;
import br.com.iteris.universidade.segundaapi.repository.CustomersRepository;
import br.com.iteris.universidade.segundaapi.repository.OrderItemsRepository;
import br.com.iteris.universidade.segundaapi.repository.OrdersRepository;
import br.com.iteris.universidade.segundaapi.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchasesService {

    private final ProductsRepository productsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CustomersRepository customersRepository;

    public ResponseBase<PurchaseResponse> cadastrar(PurchaseCreateRequest novaCompra) {
        // Entidade da nova ordem q está sendo criada
        Order novaOrder = new Order();
        // Lista de OrdemItems que fazem parte da order
        List<OrderItem> orderItems = new ArrayList<>();

        // Valida se o customer existe e caso contrario retorna um erro
        Optional<Customer> customerOptional = customersRepository.findById(
                novaCompra.getCustomerId());
        Customer customer = customerOptional
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Verifica se existe algum item na compra que está sendo feita
        if (novaCompra.getItens() == null || novaCompra.getItens().size() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A compra deve ter pelo menos 1 item de compra");
        }

        // seta o customer dessa compra
        novaOrder.setCustomer(customer);
        // Seta a data que a compra foi feita
        novaOrder.setOrderDate(LocalDateTime.now());

        // Faz um for em todos itens da compra para validar e converter de DTO para entidade
        for (PurchaseItem item : novaCompra.getItens()) {
            // Entidade da ordem item
            OrderItem orderItem = new OrderItem();

            // Verifica a quantidade de cada item
            if (item.getQuantity() < 1) {
                // se menor q 1 dá erro
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantidade do produto deve maior do que 0");
            } else {
                // se maior ou igual a 1 setá a quantidade de itens
                orderItem.setQuantity(item.getQuantity());
            }

            // Verifica se o product existe e caso contrario retorna um erroo
            Optional<Product> productOptional = productsRepository.findById(item.getProductId());
            Product product = productOptional
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto não encontrado"));

            // Verifica se o produto foir descontinuado
            if (product.getIsDiscontinued()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto descontinuado");
            }

            // Seta o produto e seu preço na entidade
            orderItem.setProduct(product);
            orderItem.setUnitPrice(product.getUnitPrice());

            // Verifica se tem desconto, se tiver unicia calculo
            if (item.getDiscount() != 0) {
                // Converte o valor do desconte de decimal para inteiro (0.1 -> 10)
                int descontoInteiro = (int) (item.getDiscount() * 100);

                // Variavel que diz se o deconto é maior que 0
                boolean descontoMaiorQueZero = descontoInteiro >= 0;

                // Variavel que diz se a quantiade de itens pe maior q 500
                boolean maisQueQuinhentos = item.getQuantity() >= 500;
                // Variavel que diz se o deconto é menor que 30
                boolean descontoAteTrinta = descontoInteiro <= 30;

                // Variavel que diz se a quantiade de itens pe maior que 100
                boolean maisQueCem = item.getQuantity() >= 100;
                // Variavel que diz se o deconto é menor que 20
                boolean descontoAteVinte = descontoInteiro <= 20;

                // Variavel que diz se a quantiade de itens pe maior que 25
                boolean maisQueVinteCinco = item.getQuantity() >= 25;
                // Variavel que diz se o deconto é menor que 10
                boolean descontoAteDez = descontoInteiro <= 10;

                // Variavel que diz se o desconto é valido usando todas as outras variaveis
                boolean isDescontoValido = descontoMaiorQueZero && (
                        (maisQueQuinhentos && descontoAteTrinta) ||
                                (maisQueCem && descontoAteVinte) ||
                                (maisQueVinteCinco && descontoAteDez)
                );

                // Verifica se o desconto é valido
                if (isDescontoValido) {
                    // Se for valido, aplica o desconto
                    double porcentagemDoPreco = 1 - item.getDiscount();
                    double precoComDesconto = orderItem.getUnitPrice() * porcentagemDoPreco;

                    orderItem.setUnitPrice(precoComDesconto);
                } else {
                    // Se não for valido retorna um erro
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Desconto inválido!");
                }
            }

            // Adiciona os itens no order itens
            orderItems.add(orderItem);
        }

        // Calcula o valor total da compra
        double valorTotal = 0;
        for (OrderItem orderItem : orderItems) {
            valorTotal += orderItem.getUnitPrice() * orderItem.getQuantity();
        }

        // Arredonda oo valor total para ter apenas duas casas depois da virgula
        double valorTotalAredondado = new BigDecimal(valorTotal)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();

        // Seta o valor total da compra
        novaOrder.setTotalAmount(valorTotalAredondado);

        // Salva a compra
        ordersRepository.save(novaOrder);

        // Seta a order para os order itens
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(novaOrder);
        }

        // Salva todos os order itens no banco
        orderItemsRepository.saveAll(orderItems);

        // retorna os dados da compra
        return new ResponseBase<>(new PurchaseResponse(novaOrder));
    }

    public ResponseBase<Page<PurchaseResponse>> pesquisar(SearchOrdersRequest searchRequest) {
        // a Pagina atual não pode ser menor que 0
        if (searchRequest.getPaginaAtual() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O indice da página atual deve começar em 0");
        }
        // a quantidade de itens por pagina deve ser entre 1 e 50
        if (searchRequest.getQtdPorPagina() < 1 || searchRequest.getQtdPorPagina() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de itens por página deve ser entre 1 e 50 itens");
        }

        PageRequest pageRequest = PageRequest.of(searchRequest.getPaginaAtual(), searchRequest.getQtdPorPagina());
        Page<Order> paginatedResponse;

        Double minPrice = searchRequest.getMinPrice();
        if (minPrice != null) {
            paginatedResponse = ordersRepository.findByTotalAmountGreaterThan(minPrice, pageRequest);
        } else {
            paginatedResponse = ordersRepository.findAll(pageRequest);
        }

        return new ResponseBase<>(paginatedResponse.map(PurchaseResponse::new));
    }
}
