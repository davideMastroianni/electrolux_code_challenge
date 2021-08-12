package it.dmastro.ecc.service.utils;

import static org.assertj.core.api.Assertions.assertThat;

import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Customer;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

class CustomerMapperTest {

  private CustomerMapper customerMapper;

  @BeforeEach
  void setUp() {
    customerMapper = new CustomerMapper();
  }

  @Test
  void givenCustomerWhenMapToDtoThenReturnCustomerDTO() {
    Customer customer = new Customer();
    customer.setAddress(RandomStringUtils.randomAlphanumeric(5));
    customer.setCity(RandomStringUtils.randomAlphanumeric(5));
    customer.setCountry(RandomStringUtils.randomAlphanumeric(5));
    customer.setFirstName(RandomStringUtils.randomAlphanumeric(5));
    customer.setLastName(RandomStringUtils.randomAlphanumeric(5));
    customer.setState(RandomStringUtils.randomAlphanumeric(5));
    customer.setZipCode(RandomStringUtils.randomAlphanumeric(5));
    customer.setId(UUID.randomUUID());

    CustomerDTO customerDTO = customerMapper.mapToDto(customer);

    assertThat(customerDTO).isNotNull();
    assertThat(customerDTO.getAddress()).isEqualTo(customer.getAddress());
    assertThat(customerDTO.getCity()).isEqualTo(customer.getCity());
    assertThat(customerDTO.getCountry()).isEqualTo(customer.getCountry());
    assertThat(customerDTO.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(customerDTO.getLastName()).isEqualTo(customer.getLastName());
    assertThat(customerDTO.getState()).isEqualTo(customer.getState());
    assertThat(customerDTO.getZipCode()).isEqualTo(customer.getZipCode());
    assertThat(customerDTO.getId()).isEqualTo(customer.getId().toString());
  }

  @Test
  void givenCustomerDTOWhenMapToEntityThenReturnCustomerEntity() {
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAddress(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setCity(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setCountry(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setFirstName(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setLastName(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setState(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setZipCode(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setId(UUID.randomUUID().toString());

    Customer customer = customerMapper.mapToEntity(customerDTO);

    assertThat(customer).isNotNull();
    assertThat(customer.getAddress()).isEqualTo(customerDTO.getAddress());
    assertThat(customer.getCity()).isEqualTo(customerDTO.getCity());
    assertThat(customer.getCountry()).isEqualTo(customerDTO.getCountry());
    assertThat(customer.getFirstName()).isEqualTo(customerDTO.getFirstName());
    assertThat(customer.getLastName()).isEqualTo(customerDTO.getLastName());
    assertThat(customer.getState()).isEqualTo(customerDTO.getState());
    assertThat(customer.getZipCode()).isEqualTo(customerDTO.getZipCode());
    assertThat(customer.getId().toString()).isEqualTo(customerDTO.getId());
  }
}