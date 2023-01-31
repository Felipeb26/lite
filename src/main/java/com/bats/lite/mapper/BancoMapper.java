package com.bats.lite.mapper;

import com.bats.lite.dto.BancoDTO;
import com.bats.lite.entity.Banco;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BancoMapper {
	public BancoDTO bancoParaDTO(Banco entity) {
		return BancoDTO.builder()
			.nome(entity.getNome())
			.conta(entity.getConta())
			.agencia(entity.getAgencia())
			.debito(entity.getDebito())
			.credito(entity.getCredito())
			.poupanca(entity.getPoupanca())
			.emprestimo(entity.getEmprestimo())
			.build();
	}

	public Banco dtoParaBanco(BancoDTO dto) {
		return Banco.builder()
			.nome(dto.getNome())
			.conta(dto.getConta())
			.agencia(dto.getAgencia())
			.debito(dto.getDebito())
			.credito(dto.getCredito())
			.poupanca(dto.getPoupanca())
			.emprestimo(dto.getEmprestimo())
			.build();
	}

	public List<BancoDTO> bancosParaDTOS(List<Banco> bancos) {
		return bancos.stream()
			.map(this::bancoParaDTO)
			.collect(Collectors.toList());
	}
	public List<Banco> dtosParaBancos(List<BancoDTO> bancoDTOS) {
		return bancoDTOS.stream()
			.map(this::dtoParaBanco)
			.collect(Collectors.toList());
	}
}
