package com.bats.lite.service;

import com.bats.lite.dto.BancoDTO;
import com.bats.lite.entity.Banco;
import com.bats.lite.entity.PageDTO;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.mapper.BancoMapper;
import com.bats.lite.repository.BancoRepository;
import com.bats.lite.specification.BancoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BancoService {

	@Autowired
	private BancoMapper bancoMapper;
	@Autowired
	private BancoRepository repository;

	public PageDTO findPaginate(Long id, String nome, Long agencia, Long conta, Pageable pageable) {
		Page<Banco> page = null;

		Specification<Banco> where = assertsSpecification(id, nome, agencia, conta);

		if (nonNull(where)) {
			page = repository.findAll(where, pageable);
		} else {
			page = repository.findAll(pageable);
		}

		return new PageDTO<>(page, bancoMapper::bancosParaDTOS);
	}

	public List<BancoDTO> findBank(Long id, String nome, Long agencia, Long conta) {
		Specification<Banco> where = assertsSpecification(id, nome, agencia, conta);

		List<BancoDTO> banco = null;
		if (nonNull(where)) {
			banco = repository.findAll(where).stream().distinct().map(bancoMapper::bancoParaDTO).collect(Collectors.toList());
		} else {
			banco = repository.findAll().stream().distinct().map(bancoMapper::bancoParaDTO).collect(Collectors.toList());
			;
		}
		if (nonNull(banco)) {
			return banco;
		}
		throw new BatsException(NOT_FOUND, "NÂO ENCONTRADO");
	}

	public Banco saveBanco(BancoDTO banco) {
		var list = findBank(null, banco.getNome(), banco.getAgencia(), banco.getConta());
		if (list.isEmpty()) {
			var bank = bancoMapper.dtoParaBanco(banco);
			repository.save(bank);

			var save = findBank(null, banco.getNome(), banco.getAgencia(), banco.getConta()).stream().findFirst();
			return bancoMapper.dtoParaBanco(save.get());
		}

		throw new BatsException(FORBIDDEN, "Banco com mesma conta e agencia já cadastrados;");
	}

	public Banco updateBanco(Long id, Banco banco) {
		var bank = repository.findAll(BancoSpecification.idIgual(id)).stream().findFirst();
		if (bank.isEmpty()) {
			throw new BatsException(NOT_FOUND, "Não encontrado usuario!");
		}

		return repository.save(banco);
	}

	public String deleteBanco(Long id) {
		Optional<Banco> bank = repository.findAll(BancoSpecification.idIgual(id)).stream().findFirst();
		if (bank.isEmpty()) {
			throw new BatsException(NOT_FOUND, "Não Encontrado Banco");
		} else {
			bank.ifPresent(banco -> repository.delete(banco));
			return "Banco Deletado com Sucesso!";
		}
	}

	private Specification<Banco> assertsSpecification(Long id, String nome, Long agencia, Long conta) {
		Specification<Banco> where = null;
		if (nonNull(id)) {
			where = BancoSpecification.idIgual(id);
		}

		if (nonNull(where) && nonNull(nome) && !nome.isEmpty()) {
			where = where.and(BancoSpecification.nomeIgual(nome));
		} else if (nonNull(nome) && !nome.isEmpty()) {
			where = BancoSpecification.nomeIgual(nome);
		}

		if (nonNull(where) && nonNull(agencia) && nonNull(conta)) {
			where = where.and(BancoSpecification.agenciaEConta(agencia, conta));
		} else if (nonNull(agencia) && nonNull(conta)) {
			where = BancoSpecification.agenciaEConta(agencia, conta);
		}
		return where;
	}

}
