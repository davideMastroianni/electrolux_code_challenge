package it.dmastro.ecc.rest;

import static it.dmastro.ecc.TestUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Appliance;
import it.dmastro.ecc.entity.Customer;
import it.dmastro.ecc.repository.ApplianceRepository;
import it.dmastro.ecc.repository.CustomerRepository;
import java.time.LocalDateTime;
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
class CustomerControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ApplianceRepository applianceRepository;

  @Autowired
  CustomerRepository customerRepository;

  Appliance appliance;

  Customer customer;

  CustomerDTO customerDTO;

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
    appliance.setConnected(true);
    appliance.setConnectionDate(LocalDateTime.now());
    appliance = applianceRepository.save(appliance);

    customerDTO = new CustomerDTO();
    customerDTO.setAddress(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setCity(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setCountry(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setFirstName(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setLastName(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setState(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setZipCode(RandomStringUtils.randomAlphanumeric(5));
    customerDTO.setId(UUID.randomUUID().toString());
  }

  @Test
  void givenCustomerIdAndIdExistsWhenGetCustomerThenResponseStatusIsOkThenReturnCustomerWithOwnedAppliances() throws Exception {
    mvc.perform(get("/api/v1/customers/{customerId}", customer.getId().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(jsonPath("$.appliances[0].applianceId").value(appliance.getApplianceId()))
        .andExpect(jsonPath("$.appliances[0].factoryNumber").value(appliance.getFactoryNumber()))
        .andExpect(jsonPath("$.appliances[0].connected").value("true"));
  }

  @Test
  void givenCustomerDTOWhenSaveCustomerThenResponseStatusIsOkThenPersistCustomer() throws Exception {
    MvcResult response = mvc.perform(post("/api/v1/customers")
            .content(asJsonString(customerDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(customerDTO.getFirstName()))
        .andReturn();

    String customerId = JsonPath.read(response.getResponse().getContentAsString(), "$.id");
    assertThat(customerId).isNotNull();
    Customer customer = customerRepository.findById(UUID.fromString(customerId))
        .orElse(null);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo(customerDTO.getFirstName());
  }


}