package it.dmastro.ecc.dataobject.customer;

import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {

  @NotBlank
  private String id;

  @NotBlank
  private String address;

  @NotBlank
  private String city;

  @NotBlank
  private String country;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String state;

  @NotBlank
  private String zipCode;

  private List<ApplianceDTO> appliances;

}
