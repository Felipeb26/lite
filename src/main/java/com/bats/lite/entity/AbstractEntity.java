package com.bats.lite.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@MappedSuperclass
@JsonIgnoreProperties(value = {"dataCadastro"}, allowGetters = true)
@EqualsAndHashCode(of = {"dataCadastro"})
public class AbstractEntity<T> implements Serializable {

	@Column(nullable = false, updatable = false)
	@CreatedDate
	protected String dataCadastro;

	@PrePersist
	public void persist() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		this.dataCadastro = dateTime.format(formatter);
	}
}