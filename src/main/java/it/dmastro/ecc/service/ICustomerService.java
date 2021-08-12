package it.dmastro.ecc.service;

import it.dmastro.ecc.dataobject.customer.CustomerDTO;

public interface ICustomerService {

  CustomerDTO getCustomer(String customerId);

  CustomerDTO saveCustomer(CustomerDTO customerDTO);

}
