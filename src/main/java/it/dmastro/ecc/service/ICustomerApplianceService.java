package it.dmastro.ecc.service;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import java.util.List;
import java.util.UUID;

public interface ICustomerApplianceService {

  List<ApplianceDTO> getCustomerAppliances(UUID customerId);

}
