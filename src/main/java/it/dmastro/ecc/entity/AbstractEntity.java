package it.dmastro.ecc.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

	@Column(name = "insert_timestamp", nullable = false)
	@CreationTimestamp
	LocalDateTime createdDate;

	@Column(name = "update_timestamp")
	@CreationTimestamp
	LocalDateTime modifiedDate;

}
