package br.com.iteris.universidade.segundaapi.repository;

import br.com.iteris.universidade.segundaapi.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Order, Long> {
    Page<Order> findByTotalAmountGreaterThan(double valorMinimo, Pageable pageable);
}
