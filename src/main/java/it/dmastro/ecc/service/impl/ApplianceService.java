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

  private final long timeToLive;

  public ApplianceService(ApplianceRepository applianceRepository,
      @Value("${appliance.time-to-live}") long timeToLive) {
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
    Appliance appliance = applianceFound.get();
    boolean connected = appliance.isConnected() && !isConnectionDateExpired(appliance);
    appliance.setConnected(connected);
    return appliance;
  }

  @Override
  public void updateApplianceConnectionTime(Appliance appliance) {
    appliance.setConnectionDate(LocalDateTime.now());
    appliance.setConnected(true);
    saveAppliance(appliance);
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

  private boolean isConnectionDateExpired(Appliance appliance) {
    LocalDateTime date = appliance.getConnectionDate();
    return LocalDateTime.now().isAfter(date.plusMinutes(timeToLive));
  }

}
