package it.dmastro.ecc.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.entity.Customer;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ApplianceControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ApplianceRepository applianceRepository;

  @Autowired
  CustomerRepository customerRepository;

  Appliance appliance;

  Customer customer;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setAddress(RandomStringUtils.randomAlphanumeric(5));
    customer.setCity(RandomStringUtils.randomAlphanumeric(5));
    customer.setCountry(RandomStringUtils.randomAlphanumeric(5));
    customer.setFirstName(RandomStringUtils.randomAlphanumeric(5));
    customer.setLastName(RandomStringUtils.randomAlphanumeric(5));
    customer.setState(RandomStringUtils.randomAlphanumeric(5));
    customer.setZipCode(RandomStringUtils.randomAlphanumeric(5));
    customer = customerRepository.save(customer);

    appliance = new Appliance();
    appliance.setCustomerId(customer.getId());
    appliance.setApplianceId(RandomStringUtils.randomAlphanumeric(36));
    appliance.setFactoryNumber(RandomStringUtils.randomAlphanumeric(36));
    appliance.setConnected(false);
    appliance = applianceRepository.save(appliance);
  }

  @Test
  void givenApplianceWhenPostSetConnectionsAndApplianceExistsThenResponseStatusIsOkThenApplianceIsConnected() throws Exception {
    mvc.perform(post("/api/v1/appliances/{applianceId}/connections", appliance.getApplianceId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value("true"))
        .andExpect(jsonPath("$.status").value("OK"));

    Appliance applianceFound = applianceRepository.findByApplianceId(appliance.getApplianceId())
        .orElse(null);
    assertThat(applianceFound).isNotNull();
    assertThat(applianceFound.isConnected()).isTrue();
  }

  @Test
  void givenApplianceWhenPostSetConnectionsAndApplianceNotExistsThenResponseStatusIsNotFound() throws Exception {
    mvc.perform(post("/api/v1/appliances/{applianceId}/connections", RandomStringUtils.randomAlphanumeric(5))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

}