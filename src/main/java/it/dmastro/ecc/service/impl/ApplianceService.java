package it.dmastro.ecc.service.impl;

import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.service.IApplianceService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplianceService implements IApplianceService {

  private final ApplianceRepository applianceRepository;

  public ApplianceService(ApplianceRepository applianceRepository) {
    this.applianceRepository = applianceRepository;
  }

  @Override
  public Appliance getAppliance(String applianceId) {
    Optional<Appliance> applianceFound = applianceRepository.findByApplianceId(applianceId);
    if (!applianceFound.isPresent()) {
      log.error(String.format("Appliance not found with id %s", applianceId));
      throw new EccNotFoundException();
    }
    return applianceFound.get();
  }

  @Override
  public void saveAppliance(Appliance appliance) {
    try {
      applianceRepository.save(appliance);
    } catch (Exception e) {
      log.error(String.format("Failed to upsert appliance %s", appliance.getApplianceId()));
      throw new EccUpsertFailedException();
    }
  }
}
