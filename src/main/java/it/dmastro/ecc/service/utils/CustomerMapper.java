package it.dmastro.ecc.service.utils;

import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Customer;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class CustomerMapper {

  public CustomerDTO mapToDto(Customer customer) {
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setAddress(customer.getAddress());
    customerDTO.setCity(customer.getCity());
    customerDTO.setCountry(customer.getCountry());
    customerDTO.setFirstName(customer.getFirstName());
    customerDTO.setLastName(customer.getLastName());
    customerDTO.setState(customer.getState());
    customerDTO.setZipCode(customer.getZipCode());
    customerDTO.setId(customer.getId().toString());
    return customerDTO;
  }

  public Customer mapToEntity(CustomerDTO customerDTO) {
    Customer customer = new Customer();
    customer.setAddress(customerDTO.getAddress());
    customer.setCity(customerDTO.getCity());
    customer.setCountry(customerDTO.getCountry());
    customer.setFirstName(customerDTO.getFirstName());
    customer.setLastName(customerDTO.getLastName());
    customer.setState(customerDTO.getState());
    customer.setZipCode(customerDTO.getZipCode());
    if (StringUtils.hasText(customerDTO.getId())) {
      customer.setId(UUID.fromString(customerDTO.getId()));
    }
    return customer;
  }


}
