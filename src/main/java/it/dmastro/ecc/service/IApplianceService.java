package it.dmastro.ecc.service;

import it.dmastro.ecc.entity.Appliance;

public interface IApplianceService {

  Appliance getAppliance(String applianceId);

  void updateApplianceConnectionTime(Appliance appliance);

  void saveAppliance(Appliance appliance);

}
