package it.dmastro.ecc.service.impl;

import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.service.IApplianceService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplianceService implements IApplianceService {

  private final ApplianceRepository applianceRepository;

  private final String timeToLive;

  public ApplianceService(ApplianceRepository applianceRepository,
      @Value("${appliance.time-to-live}") String timeToLive) {
    this.applianceRepository = applianceRepository;
    this.timeToLive = timeToLive;
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
      appliance.setModifiedDate(LocalDateTime.now());
      applianceRepository.save(appliance);
    } catch (Exception e) {
      log.error(String.format("Failed to upsert appliance %s", appliance.getApplianceId()));
      throw new EccUpsertFailedException();
    }
  }
}
