package br.com.iteris.universidade.segundaapi.repository;


import br.com.iteris.universidade.segundaapi.domain.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends PagingAndSortingRepository<Customer, Long> {
}
