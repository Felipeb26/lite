package com.bats.lite.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QeueEntity extends AbstractEntity<QeueEntity> {

	@Id
	private Long id;
	private String message;
	private Boolean valid;

}
