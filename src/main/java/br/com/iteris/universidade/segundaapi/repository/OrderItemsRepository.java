package br.com.iteris.universidade.segundaapi.repository;

import br.com.iteris.universidade.segundaapi.domain.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends CrudRepository<OrderItem, Long> {
}
