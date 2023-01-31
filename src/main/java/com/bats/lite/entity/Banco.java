package com.bats.lite.entity;

import lombok.*;

import javax.persistence.Entity;

@ToString
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Banco extends AbstractEntity<Banco> {

	private String nome;
	private Long conta;
	private Long agencia;
	private int debito;
	private int credito;
	private int poupanca;
	private int emprestimo;

}
