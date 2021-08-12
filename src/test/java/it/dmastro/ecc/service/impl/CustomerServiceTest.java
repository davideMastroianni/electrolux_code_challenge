package it.dmastro.ecc.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Customer;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.CustomerRepository;
import it.dmastro.ecc.service.ICustomerApplianceService;
import it.dmastro.ecc.service.utils.CustomerMapper;
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
class CustomerServiceTest {

  @Mock
  CustomerRepository customerRepository;

  @Mock
  ICustomerApplianceService applianceService;

  @Spy
  CustomerMapper customerMapper;

  @Captor
  ArgumentCaptor<Customer> customerCaptor;

  CustomerService customerService;

  ApplianceDTO applianceDTO;

  Customer customer;

  CustomerDTO customerDTO;

  @BeforeEach
  void setUp() {
    customerService = new CustomerService(customerRepository, customerMapper, applianceService);

    customer = new Customer();
    customer.setAddress(RandomStringUtils.randomAlphanumeric(5));
    customer.setCity(RandomStringUtils.randomAlphanumeric(5));
    customer.setCountry(RandomStringUtils.randomAlphanumeric(5));
    customer.setFirstName(RandomStringUtils.randomAlphanumeric(5));
    customer.setLastName(RandomStringUtils.randomAlphanumeric(5));
    customer.setState(RandomStringUtils.randomAlphanumeric(5));
    customer.setZipCode(RandomStringUtils.randomAlphanumeric(5));
    customer.setId(UUID.randomUUID());

    applianceDTO = new ApplianceDTO();
    applianceDTO.setId(UUID.randomUUID().toString());
    applianceDTO.setCustomerId(customer.getId().toString());
    applianceDTO.setApplianceId(RandomStringUtils.randomAlphanumeric(3));
    applianceDTO.setFactoryNumber(RandomStringUtils.randomAlphanumeric(3));
    applianceDTO.setConnected(true);
    applianceDTO.setConnectionDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    customerDTO = new CustomerDTO();
    customerDTO.setAddress(customer.getAddress());
    customerDTO.setCity(customer.getCity());
    customerDTO.setCountry(customer.getCountry());
    customerDTO.setFirstName(customer.getFirstName());
    customerDTO.setLastName(customer.getLastName());
    customerDTO.setState(customer.getState());
    customerDTO.setZipCode(customer.getZipCode());
    customerDTO.setId(customer.getId().toString());
  }

  @Test
  void givenCustomerIdWhenGetCustomerThenReturnCustomerWithAppliances() {
    List<ApplianceDTO> appliances = new ArrayList<>();
    appliances.add(applianceDTO);

    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
    given(applianceService.getCustomerAppliances(customer.getId())).willReturn(appliances);

    CustomerDTO result = customerService.getCustomer(customer.getId().toString());

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(customer.getId().toString());
    assertThat(result.getAppliances().size()).isEqualTo(1);
    assertThat(result.getAppliances().get(0).getId()).isEqualTo(applianceDTO.getId());
    verify(customerRepository, times(1)).findById(customer.getId());
    verify(applianceService, times(1)).getCustomerAppliances(customer.getId());
  }

  @Test
  void givenCustomerIdAndCustomerNotExistsWhenGetCustomerThenThrowEccNotFoundException() {
    UUID falseCustomerId = UUID.randomUUID();
    given(customerRepository.findById(falseCustomerId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> customerService.getCustomer(falseCustomerId.toString()))
        .isInstanceOf(EccNotFoundException.class);
    verify(customerRepository, times(1)).findById(falseCustomerId);
  }


  @Test
  void givenValidCustomerWhenSaveCustomerThenUpdateModifyDateThenPersistEntity() {
    given(customerRepository.save(customerCaptor.capture())).willReturn(customer);

    customerService.saveCustomer(customerDTO);

    Customer captureAppliance = customerCaptor.getValue();
    assertThat(captureAppliance.getModifiedDate()).isNotNull();
    verify(customerRepository, times(1)).save(captureAppliance);
  }

  @Test
  void givenInvalidCustomerWhenSaveCustomerThenPersistEntityWithoutModifying() {
    given(customerRepository.save(customerCaptor.capture())).willThrow(new EccUpsertFailedException());

    assertThatThrownBy(() -> customerService.saveCustomer(customerDTO))
        .isInstanceOf(EccUpsertFailedException.class);

    verify(customerRepository, times(1)).save(customerCaptor.getValue());
  }


}