package it.dmastro.ecc.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

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

	@Column(name = "insert_user", nullable = false)
	String creationUser;

	@Column(name = "update_user")
	String modificationUser;

	@Column(name = "version", nullable = false)
	String version;

	public AbstractEntity(@Value("${spring.application.name}") String creationUser,
			@Value("${spring.application.database.version}") String version) {
		this.creationUser = creationUser;
		this.version = version;
	}

}
