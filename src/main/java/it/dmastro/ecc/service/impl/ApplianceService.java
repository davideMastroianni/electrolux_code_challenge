package it.dmastro.ecc.service.impl;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.service.IApplianceService;
import it.dmastro.ecc.service.ICustomerApplianceService;
import it.dmastro.ecc.service.utils.ApplianceMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplianceService implements IApplianceService, ICustomerApplianceService {

  private final ApplianceRepository applianceRepository;

  private final ApplianceMapper applianceMapper;

  private final long timeToLive;

  public ApplianceService(ApplianceRepository applianceRepository, ApplianceMapper applianceMapper,
      @Value("${appliance.time-to-live}") long timeToLive) {
    this.applianceRepository = applianceRepository;
    this.applianceMapper = applianceMapper;
    this.timeToLive = timeToLive;
  }

  @Override
  public ApplianceDTO getAppliance(String applianceId) {
    Optional<Appliance> applianceFound = applianceRepository.findByApplianceId(applianceId);
    if (!applianceFound.isPresent()) {
      log.error(String.format("Appliance not found with id %s", applianceId));
      throw new EccNotFoundException();
    }
    Appliance appliance = applianceFound.get();
    boolean connected = appliance.isConnected() && !isConnectionDateExpired(appliance);
    appliance.setConnected(connected);
    return applianceMapper.mapToDto(appliance);
  }

  @Override
  public void updateApplianceConnectionTime(ApplianceDTO applianceDTO) {
    Appliance appliance = applianceMapper.mapToEntity(applianceDTO);
    appliance.setConnectionDate(LocalDateTime.now());
    appliance.setConnected(true);
    saveAppliance(appliance);
  }

  @Override
  public void saveAppliance(ApplianceDTO applianceDTO) {
    Appliance appliance = applianceMapper.mapToEntity(applianceDTO);
    saveAppliance(appliance);
  }

  private void saveAppliance(Appliance appliance) {
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

  @Override
  public List<ApplianceDTO> getCustomerAppliances(UUID customerId) {
    List<Appliance> appliances = applianceRepository.findAllByCustomerId(customerId);
    appliances.forEach(appliance -> {
      boolean connected = appliance.isConnected() && !isConnectionDateExpired(appliance);
      appliance.setConnected(connected);
    });
    return appliances.stream()
        .map(applianceMapper::mapToDto)
        .collect(Collectors.toList());
  }
}
