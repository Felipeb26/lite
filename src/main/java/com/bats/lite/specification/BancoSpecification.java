package com.bats.lite.specification;

import com.bats.lite.entity.Banco;
import com.bats.lite.entity.Banco_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class BancoSpecification {

	public Specification<Banco> idIgual(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Banco_.ID), id);
	}

	public Specification<Banco> nomeIgual(String nome) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Banco_.NOME), "%" + nome + "%");
	}

	public Specification<Banco> agenciaEConta(Long agencia, Long conta) {
		return (root, query, criteriaBuilder) -> {
			var agenciaPredicate = criteriaBuilder.equal(root.get(Banco_.AGENCIA), agencia);
			var contaPredicate = criteriaBuilder.equal(root.get(Banco_.CONTA), conta);
			return criteriaBuilder.and(agenciaPredicate, contaPredicate);
		};
	}

}
