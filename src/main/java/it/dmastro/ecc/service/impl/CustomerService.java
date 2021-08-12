package it.dmastro.ecc.service.impl;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.entity.Customer;
import it.dmastro.ecc.exceptions.EccNotFoundException;
import it.dmastro.ecc.exceptions.EccUpsertFailedException;
import it.dmastro.ecc.repository.CustomerRepository;
import it.dmastro.ecc.service.ICustomerApplianceService;
import it.dmastro.ecc.service.ICustomerService;
import it.dmastro.ecc.service.utils.CustomerMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerService implements ICustomerService {

  private final CustomerRepository customerRepository;

  private final CustomerMapper customerMapper;
  
  private final ICustomerApplianceService applianceService;

  public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, ICustomerApplianceService applianceService) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
    this.applianceService = applianceService;
  }

  @Override
  public CustomerDTO getCustomer(String customerId) {
    Optional<Customer> customerFound = customerRepository.findById(UUID.fromString(customerId));
    if (!customerFound.isPresent()) {
      log.error(String.format("Appliance not found with id %s", customerId));
      throw new EccNotFoundException();
    }
    CustomerDTO customer = customerMapper.mapToDto(customerFound.get());
    List<ApplianceDTO> appliances = applianceService.getCustomerAppliances(customerFound.get().getId());
    customer.setAppliances(appliances);
    return customer;
  }

  @Override
  public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
    Customer customer = customerMapper.mapToEntity(customerDTO);
    customer = saveCustomer(customer);
    return customerMapper.mapToDto(customer);
  }

  private Customer saveCustomer(Customer customer) {
    try {
      customer.setModifiedDate(LocalDateTime.now());
      return customerRepository.save(customer);
    } catch (Exception e) {
      log.error(String.format("Failed to upsert customer %s", customer.getId().toString()));
      throw new EccUpsertFailedException();
    }
  }
}
