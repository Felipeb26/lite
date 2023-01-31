package com.bats.lite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BancoDTO {

	private String nome;
	private Long conta;
	private Long agencia;
	private int debito;
	private int credito;
	private int poupanca;
	private int emprestimo;

}
