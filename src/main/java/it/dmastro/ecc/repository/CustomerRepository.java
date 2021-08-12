package it.dmastro.ecc.repository;

import it.dmastro.ecc.entity.Customer;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

}
