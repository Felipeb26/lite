package com.bats.lite.service;

import com.bats.lite.entity.PageDTO;
import com.bats.lite.entity.User;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.repository.UserRepository;
import com.bats.lite.specification.UserSpecification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private RabbitTemplate template;

	public List<User> findByParam(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal) {
		Specification<User> where = Specification.where(UserSpecification.exists());

		if (nonNull(id)) {
			where = where.and(UserSpecification.iDEguals(id));
		}
		if (nonNull(nome) && !nome.isBlank()) {
			where = where.and(UserSpecification.nomeEquals(nome));
		}
		if (nonNull(email) && !email.isBlank()) {
			where = where.and(UserSpecification.emailEquals(email));
		}
		if (nonNull(dataInicial)) {
			if (nonNull(dataFinal)) {
				where = where.and(UserSpecification.nascimentoBetween(dataInicial, dataFinal));
			} else {
				where = where.and(UserSpecification.nascimentoEquals(dataInicial));
			}
		}
		var list = repository.findAll(where);
		if (list.isEmpty()) {
			throw new BatsException(NO_CONTENT, "Sem usuarios encontrados.");
		}
		return list;
	}

	public PageDTO findByParamPaged(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
		Specification<User> where = Specification.where(UserSpecification.exists());

		if (nonNull(id)) {
			where = where.and(UserSpecification.iDEguals(id));
		}
		if (nonNull(nome) && !nome.isBlank()) {
			where = where.and(UserSpecification.nomeEquals(nome));
		}
		if (nonNull(email) && !email.isBlank()) {
			where = where.and(UserSpecification.emailEquals(email));
		}
		if (nonNull(dataInicial)) {
			if (nonNull(dataFinal)) {
				where = where.and(UserSpecification.nascimentoBetween(dataInicial, dataFinal));
			} else {
				where = where.and(UserSpecification.nascimentoEquals(dataInicial));
			}
		}
		var page = repository.findAll(where, pageable);
		if (page.isEmpty()) {
			throw new BatsException(NO_CONTENT, "Sem usuarios cadastrados");
		}

		return new PageDTO<>(page);
	}

	public User save(User user) {
		Specification<User> where = Specification.where(UserSpecification.emailEquals(user.getEmail()));
		var list = repository.findAll(where);

		var size = repository.findAll();

		if (list.isEmpty()) {
			var usuario = User.builder()
				.id(size.stream().count()+ 1)
				.nome(user.getNome())
				.email(user.getEmail())
				.nascimento(user.getNascimento())
				.build();

			template.convertAndSend("usuario.cadastrado", usuario);
			return repository.save(usuario);
		}
		throw new BatsException(BAD_REQUEST, "Email já cadastrado");
	}

	public User update(Long id, User user) {
		Specification<User> where = Specification.where(UserSpecification.iDEguals(id));
		var list = repository.findAll(where);
		if (list.isEmpty()) {
			throw new BatsException(NOT_FOUND, "não encontrado");
		}
		return repository.save(user);
	}

	public String delete(Long id) {
		Specification<User> where = null;
		if (nonNull(id)) {
			where = Specification.where(UserSpecification.iDEguals(id));
			var user = repository.findAll(where);
			if (!user.isEmpty()) {
				repository.delete(user.get(0));
				return "Usuario deletado com sucesso!";
			} else {
				throw new BatsException(NOT_FOUND, "não encontrado usuario");
			}
		} else {
			throw new BatsException(NOT_FOUND, "id não encontrado");
		}
	}


}