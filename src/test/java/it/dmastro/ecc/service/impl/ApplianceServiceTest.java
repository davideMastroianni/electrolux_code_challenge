package it.dmastro.ecc.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.service.utils.ApplianceMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceTest {

  @Mock
  ApplianceRepository applianceRepository;

  @Spy
  ApplianceMapper applianceMapper;

  @Captor
  ArgumentCaptor<Appliance> applianceCaptor;

  ApplianceService applianceService;

  Appliance appliance;

  ApplianceDTO applianceDTO;

  long timeToLive;

  @BeforeEach
  void setUp() {
    timeToLive = 1;
    applianceService = new ApplianceService(applianceRepository, applianceMapper, timeToLive);

    appliance = new Appliance();
    appliance.setId(UUID.randomUUID());
    appliance.setCustomerId(UUID.randomUUID());
    appliance.setApplianceId(RandomStringUtils.randomAlphanumeric(36));
    appliance.setFactoryNumber(RandomStringUtils.randomAlphanumeric(36));
    appliance.setConnectionDate(LocalDateTime.now());
    appliance.setConnected(false);

    applianceDTO = new ApplianceDTO();
    applianceDTO.setCustomerId(appliance.getCustomerId().toString());
    applianceDTO.setApplianceId(appliance.getApplianceId());
    applianceDTO.setFactoryNumber(appliance.getFactoryNumber());
    applianceDTO.setConnected(applianceDTO.isConnected());
    applianceDTO.setConnectionDate(appliance.getConnectionDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  @Test
  void givenApplianceIdAndApplianceExistsAndApplianceIsNotConnectedWhenGetApplianceThenReturnAppliance() {
    given(applianceRepository.findByApplianceId(appliance.getApplianceId())).willReturn(Optional.of(appliance));

    ApplianceDTO result = applianceService.getAppliance(appliance.getApplianceId());

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(appliance.getId().toString());
    assertThat(result.isConnected()).isFalse();
    verify(applianceRepository, times(1)).findByApplianceId(appliance.getApplianceId());
  }

  @Test
  void givenApplianceAndApplianceIsConnectedAndNotExpiredWhenGetApplianceThenReturnApplianceThenIsConnected() {
    appliance.setConnected(true);
    appliance.setConnectionDate(LocalDateTime.now());
    given(applianceRepository.findByApplianceId(appliance.getApplianceId())).willReturn(Optional.of(appliance));

    ApplianceDTO result = applianceService.getAppliance(appliance.getApplianceId());

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(appliance.getId().toString());
    assertThat(result.isConnected()).isTrue();
    verify(applianceRepository, times(1)).findByApplianceId(appliance.getApplianceId());
  }

  @Test
  void givenApplianceAndApplianceIsConnectedAndExpiredWhenGetApplianceThenReturnApplianceThenIsConnected() {
    appliance.setConnected(true);
    appliance.setConnectionDate(LocalDateTime.now().plusMinutes(-2));
    given(applianceRepository.findByApplianceId(appliance.getApplianceId())).willReturn(Optional.of(appliance));

    ApplianceDTO result = applianceService.getAppliance(appliance.getApplianceId());

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(appliance.getId().toString());
    assertThat(result.isConnected()).isFalse();
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
  void givenValidApplianceWhenSaveApplianceThenUpdateModifyDateThenPersistEntity() {
    given(applianceRepository.save(applianceCaptor.capture())).willReturn(appliance);

    applianceService.saveAppliance(applianceDTO);

    Appliance captureAppliance = applianceCaptor.getValue();
    assertThat(captureAppliance.getModifiedDate()).isNotNull();
    verify(applianceRepository, times(1)).save(captureAppliance);
  }

  @Test
  void givenInvalidApplianceWhenSaveApplianceThenPersistEntityWithoutModifying() {
    given(applianceRepository.save(applianceCaptor.capture())).willThrow(new EccUpsertFailedException());

    assertThatThrownBy(() -> applianceService.saveAppliance(applianceDTO))
        .isInstanceOf(EccUpsertFailedException.class);

    verify(applianceRepository, times(1)).save(applianceCaptor.getValue());
  }

  @Test
  void givenApplianceWhenUpdateApplianceConnectionTimeThenIsConnectedTrueThenConnectionDateIsUpdated() {
    given(applianceRepository.save(applianceCaptor.capture())).willReturn(appliance);

    applianceService.updateApplianceConnectionTime(applianceDTO);

    Appliance captureAppliance = applianceCaptor.getValue();
    assertThat(captureAppliance.getConnectionDate()).isNotNull();
    assertThat(captureAppliance.isConnected()).isTrue();
    verify(applianceRepository, times(1)).save(captureAppliance);
  }

  @Test
  void givenApplianceListWhenGetCustomerAppliancesThenReturnAppliancesDtoThenConnectionIsFalse() {
    appliance.setConnected(true);
    appliance.setConnectionDate(LocalDateTime.now().plusMinutes(-2));
    List<Appliance> appliances = new ArrayList<>();
    appliances.add(appliance);

    given(applianceRepository.findAllByCustomerId(appliance.getCustomerId())).willReturn(appliances);

    List<ApplianceDTO> mappedAppliances = applianceService.getCustomerAppliances(appliance.getCustomerId());

    assertThat(mappedAppliances).isNotEmpty();
    assertThat(mappedAppliances.get(0).isConnected()).isFalse();
    verify(applianceRepository, times(1)).findAllByCustomerId(appliance.getCustomerId());
    verify(applianceMapper, times(1)).mapToDto(appliance);
  }

  @Test
  void givenApplianceListWhenGetCustomerAppliancesThenReturnAppliancesDtoThenConnectionIsTrue() {
    appliance.setConnected(true);
    appliance.setConnectionDate(LocalDateTime.now());
    List<Appliance> appliances = new ArrayList<>();
    appliances.add(appliance);

    given(applianceRepository.findAllByCustomerId(appliance.getCustomerId())).willReturn(appliances);

    List<ApplianceDTO> mappedAppliances = applianceService.getCustomerAppliances(appliance.getCustomerId());

    assertThat(mappedAppliances).isNotEmpty();
    assertThat(mappedAppliances.get(0).isConnected()).isTrue();
    verify(applianceRepository, times(1)).findAllByCustomerId(appliance.getCustomerId());
    verify(applianceMapper, times(1)).mapToDto(appliance);
  }

}