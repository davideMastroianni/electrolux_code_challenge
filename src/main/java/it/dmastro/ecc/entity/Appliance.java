package it.dmastro.ecc.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appliance")
public class Appliance extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  UUID id;

  @Column(name = "customer_id", nullable = false)
  UUID customerId;

  @Column(name = "appliance_id", nullable = false)
  String applianceId;

  @Column(name = "factory_number", nullable = false)
  String factoryNumber;

  @Column(name = "is_connected", nullable = false)
  boolean isConnected = false;


}
