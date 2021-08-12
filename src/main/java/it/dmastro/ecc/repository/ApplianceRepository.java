package it.dmastro.ecc.repository;

import it.dmastro.ecc.entity.Appliance;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ApplianceRepository extends CrudRepository<Appliance, UUID> {

}
