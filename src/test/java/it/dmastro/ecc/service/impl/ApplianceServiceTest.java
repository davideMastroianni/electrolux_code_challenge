package it.dmastro.ecc.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.ApplianceRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceTest {

  @Mock
  ApplianceRepository applianceRepository;

  @Captor
  ArgumentCaptor<Appliance> applianceCaptor;

  ApplianceService applianceService;

  Appliance appliance;

  @BeforeEach
  void setUp() {
    applianceService = new ApplianceService(applianceRepository);

    appliance = new Appliance();
    appliance.setCustomerId(UUID.randomUUID());
    appliance.setApplianceId(RandomStringUtils.randomAlphanumeric(36));
    appliance.setFactoryNumber(RandomStringUtils.randomAlphanumeric(36));
    appliance.setConnected(false);
  }

  @Test
  void givenApplianceIdAndApplianceExistsWhenGetApplianceThenReturnAppliance() {
    given(applianceRepository.findByApplianceId(appliance.getApplianceId())).willReturn(Optional.of(appliance));

    Appliance result = applianceService.getAppliance(appliance.getApplianceId());

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(appliance.getId());
    verify(applianceRepository, times(1)).findByApplianceId(appliance.getApplianceId());
  }

  @Test
  void givenApplianceIdAndApplianceNotExistsWhenGetApplianceThenThrowEccNotFoundException() {
    String falseApplianceId = RandomStringUtils.random(5);
    given(applianceRepository.findByApplianceId(falseApplianceId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> applianceService.getAppliance(falseApplianceId))
        .isInstanceOf(EccNotFoundException.class);
    verify(applianceRepository, times(1)).findByApplianceId(falseApplianceId);
  }

  @Test
  void givenValidApplianceWhenSaveApplianceThenPersistEntityWithoutModifying() {
    given(applianceRepository.save(applianceCaptor.capture())).willReturn(appliance);

    applianceService.saveAppliance(appliance);

    Appliance captureAppliance = applianceCaptor.getValue();
    assertThat(captureAppliance).isEqualTo(appliance);
    verify(applianceRepository, times(1)).save(captureAppliance);
  }

  @Test
  void givenInvalidApplianceWhenSaveApplianceThenPersistEntityWithoutModifying() {
    given(applianceRepository.save(applianceCaptor.capture())).willThrow(new EccUpsertFailedException());

    assertThatThrownBy(() -> applianceService.saveAppliance(appliance))
        .isInstanceOf(EccUpsertFailedException.class);

    verify(applianceRepository, times(1)).save(applianceCaptor.getValue());
  }
}