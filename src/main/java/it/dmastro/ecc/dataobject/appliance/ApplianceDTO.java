package it.dmastro.ecc.dataobject.appliance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApplianceDTO implements Serializable {

  @NotBlank
  String Id;

  @NotBlank
  String customerId;

  @NotBlank
  String applianceId;

  @NotBlank
  String factoryNumber;

  boolean isConnected;

  String connectionDate;

}
