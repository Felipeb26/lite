package com.bats.lite.mapper;

import com.bats.lite.dto.BancoDTO;
import com.bats.lite.entity.Banco;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BancoMapper {

	public static BancoDTO bancoParaDTO(Banco entity) {
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
	public static Banco dtoParaBanco(BancoDTO dto) {
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
	public static List<BancoDTO> bancosParaDTOS(List<Banco> bancos) {
		return bancos.stream()
			.map(BancoMapper::bancoParaDTO)
			.collect(Collectors.toList());
	}
	public static List<Banco> dtosParaBancos(List<BancoDTO> bancoDTOS) {
		return bancoDTOS.stream()
			.map(BancoMapper::dtoParaBanco)
			.collect(Collectors.toList());
	}
}
