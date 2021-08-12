package it.dmastro.ecc.service.utils;

import static org.assertj.core.api.Assertions.assertThat;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.entity.Customer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

class ApplianceMapperTest {

  private ApplianceMapper applianceMapper;

  @BeforeEach
  void setUp() {
    applianceMapper = new ApplianceMapper();
  }

  @Test
  void givenApplianceWhenMapToDtoThenReturnApplianceDTO() {
    Appliance appliance = new Appliance();
    appliance.setId(UUID.randomUUID());
    appliance.setCustomerId(UUID.randomUUID());
    appliance.setApplianceId(RandomStringUtils.randomAlphanumeric(36));
    appliance.setFactoryNumber(RandomStringUtils.randomAlphanumeric(36));
    appliance.setConnectionDate(LocalDateTime.now());
    appliance.setConnected(false);

    ApplianceDTO applianceDTO = applianceMapper.mapToDto(appliance);

    assertThat(applianceDTO).isNotNull();
    assertThat(applianceDTO.getApplianceId()).isEqualTo(appliance.getApplianceId());
    assertThat(applianceDTO.getCustomerId()).isEqualTo(appliance.getCustomerId().toString());
    assertThat(applianceDTO.getConnectionDate())
        .isEqualTo(appliance.getConnectionDate().format(DateTimeFormatter.ISO_DATE_TIME));
    assertThat(applianceDTO.isConnected()).isEqualTo(appliance.isConnected());
    assertThat(applianceDTO.getId()).isEqualTo(appliance.getId().toString());
  }

  @Test
  void givenCustomerDTOWhenMapToEntityThenReturnCustomerEntity() {
    ApplianceDTO applianceDTO = new ApplianceDTO();
    applianceDTO.setId(UUID.randomUUID().toString());
    applianceDTO.setCustomerId(UUID.randomUUID().toString());
    applianceDTO.setApplianceId(RandomStringUtils.randomAlphanumeric(36));
    applianceDTO.setFactoryNumber(RandomStringUtils.randomAlphanumeric(36));
    applianceDTO.setConnectionDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    applianceDTO.setConnected(false);

    Appliance appliance = applianceMapper.mapToEntity(applianceDTO);

    assertThat(appliance).isNotNull();
    assertThat(appliance.getApplianceId()).isEqualTo(applianceDTO.getApplianceId());
    assertThat(appliance.getCustomerId().toString()).isEqualTo(applianceDTO.getCustomerId());
    assertThat(appliance.getConnectionDate().format(DateTimeFormatter.ISO_DATE_TIME))
        .isEqualTo(applianceDTO.getConnectionDate());
    assertThat(appliance.isConnected()).isEqualTo(applianceDTO.isConnected());
    assertThat(appliance.getId().toString()).isEqualTo(applianceDTO.getId());
  }
}