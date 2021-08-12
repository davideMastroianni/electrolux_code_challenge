package it.dmastro.ecc.service.utils;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.entity.Appliance;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplianceMapper {

  public ApplianceDTO mapToDto(Appliance appliance) {
    ApplianceDTO applianceDTO = new ApplianceDTO();
    applianceDTO.setId(appliance.getId().toString());
    applianceDTO.setApplianceId(appliance.getApplianceId());
    applianceDTO.setCustomerId(appliance.getCustomerId().toString());
    applianceDTO.setConnected(appliance.isConnected());
    applianceDTO.setConnectionDate(appliance.getConnectionDate().format(DateTimeFormatter.ISO_DATE_TIME));
    applianceDTO.setFactoryNumber(appliance.getFactoryNumber());
    return applianceDTO;
  }

  public Appliance mapToEntity(ApplianceDTO applianceDTO) {
    Appliance appliance = new Appliance();
    if (applianceDTO.getId() != null) {
      appliance.setId(UUID.fromString(applianceDTO.getId()));
    }
    if (applianceDTO.getCustomerId() != null) {
      appliance.setCustomerId(UUID.fromString(applianceDTO.getCustomerId()));
    }
    appliance.setApplianceId(applianceDTO.getApplianceId());
    appliance.setConnected(applianceDTO.isConnected());
    appliance.setConnectionDate(LocalDateTime.parse(applianceDTO.getConnectionDate(), DateTimeFormatter.ISO_DATE_TIME));
    appliance.setFactoryNumber(applianceDTO.getFactoryNumber());
    return appliance;
  }

}
