package com.bats.lite.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QeueEntity extends AbstractEntity<QeueEntity> {

	private String message;
	private Boolean valid;

}
