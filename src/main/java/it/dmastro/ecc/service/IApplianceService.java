package it.dmastro.ecc.service;


import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;

public interface IApplianceService {

  ApplianceDTO getAppliance(String applianceId);

  void updateApplianceConnectionTime(ApplianceDTO appliance);

  ApplianceDTO saveAppliance(ApplianceDTO appliance);

}
