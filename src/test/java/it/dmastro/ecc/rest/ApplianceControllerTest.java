package it.dmastro.ecc.rest;

import static it.dmastro.ecc.TestUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.entity.Customer;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.repository.CustomerRepository;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

  ApplianceDTO applianceDTO;

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

    applianceDTO = new ApplianceDTO();
    applianceDTO.setCustomerId(appliance.getCustomerId().toString());
    applianceDTO.setApplianceId(appliance.getApplianceId());
    applianceDTO.setFactoryNumber(appliance.getFactoryNumber());
    applianceDTO.setConnected(applianceDTO.isConnected());
    applianceDTO.setConnectionDate(appliance.getConnectionDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  @Test
  void givenApplianceWhenPostSetConnectionsAndApplianceExistsThenResponseStatusIsOkThenApplianceIsConnected() throws Exception {
    mvc.perform(post("/api/v1/appliances/{applianceId}/connections", appliance.getApplianceId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
        .andExpect(jsonPath("$.applianceId").value(appliance.getApplianceId()))
        .andExpect(jsonPath("$.factoryNumber").value(appliance.getFactoryNumber()))
        .andExpect(jsonPath("$.connected").value("false"));

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

  @Test
  void givenApplianceDTOWhenSaveApplianceThenResponseStatusIsOkThenPersistAppliance() throws Exception {
    MvcResult response = mvc.perform(post("/api/v1/appliances")
            .content(asJsonString(applianceDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.applianceId").value(applianceDTO.getApplianceId()))
        .andExpect(jsonPath("$.factoryNumber").value(applianceDTO.getFactoryNumber()))
        .andReturn();

    String applianceId = JsonPath.read(response.getResponse().getContentAsString(), "$.id");
    assertThat(applianceId).isNotNull();
    Appliance appliance = applianceRepository.findById(UUID.fromString(applianceId))
        .orElse(null);
    assertThat(appliance).isNotNull();
    assertThat(appliance.getApplianceId()).isEqualTo(applianceDTO.getApplianceId());
  }

}