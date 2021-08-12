package it.dmastro.ecc.repository;

import it.dmastro.ecc.entity.Appliance;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ApplianceRepository extends CrudRepository<Appliance, UUID> {

  @Query("select a from Appliance a where a.applianceId = ?1")
  Optional<Appliance> findByApplianceId(String applianceId);

  @Query("select a from Appliance a where a.customerId = ?1")
  List<Appliance> findAllByCustomerId(UUID fromString);
}
