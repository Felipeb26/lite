package com.bats.lite.service.Implements;

import com.bats.lite.dto.UserDTO;
import com.bats.lite.entity.Login;
import com.bats.lite.entity.PageDTO;
import com.bats.lite.entity.User;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.mapper.UserMapper;
import com.bats.lite.repository.LoginRepository;
import com.bats.lite.repository.UserRepository;
import com.bats.lite.service.UserService;
import com.bats.lite.specification.UserSpecification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Service
public class UserServiceImplements implements UserService {

    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private LoginRepository loginRepository;

    public List<UserDTO> findByParam(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal) {
        Specification<User> where = null;

        where = SpecificationNull(where, id, nome, email, dataInicial, dataFinal);
        final List<User> users;
        if (nonNull(where)) {
            users = repository.findAll(where);
        } else {
            users = repository.findAll();
        }
        if (users.isEmpty()) {
            throw new BatsException(NO_CONTENT, "Sem usuarios encontrados.");
        }
        return toListDTO(users);
    }

    public PageDTO findByParamPaged(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        Specification<User> where = null;

        where = SpecificationNull(where, id, nome, email, dataInicial, dataFinal);
        final Page<User> page;
        if (nonNull(where)) {
            page = repository.findAll(where, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        if (page.isEmpty()) {
            throw new BatsException(NO_CONTENT, "Sem usuarios cadastrados");
        }

        return new PageDTO<>(page, this::toListDTO);
    }

    public List<UserDTO> toListDTO(List<User> users) {
        List<UserDTO> dtos = new ArrayList<>();
        users.forEach(it -> dtos.add(mapper.toDTO(it)));
        return dtos;
    }

    public UserDTO save(UserDTO user) {
        Specification<User> where = Specification.where(UserSpecification.emailEquals(user.getEmail()));
        var list = repository.findAll(where);

        if (list.isEmpty()) {
            var login = Login.builder()
                    .email(user.getEmail())
                    .senha(user.getSenha()).build();

            var usuario = mapper.toEntity(user, login);
            template.convertAndSend("usuario.cadastrado", mapper.toDTO(repository.save(usuario)));
            loginRepository.save(login);
            return mapper.toDTO(repository.save(usuario));
        }
        throw new BatsException(BAD_REQUEST, "Email já cadastrado");
    }

    public UserDTO update(Long id, UserDTO user) {
        Specification<User> where = Specification.where(UserSpecification.iDEguals(id));
        var list = repository.findAll(where);
        if (list.isEmpty()) {
            throw new BatsException(NOT_FOUND, "não encontrado");
        }
        var userDTO = repository.save(mapper.toEntity(user));
        return mapper.toDTO(userDTO);
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

    public Specification<User> SpecificationNull(Specification<User> where, Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal) {

        if (nonNull(id) && nonNull(where)) {
            where = where.and(UserSpecification.iDEguals(id));
        } else if (nonNull(id)) {
            where = UserSpecification.iDEguals(id);
        }

        if (nonNull(nome) && !nome.isBlank() && nonNull(where)) {
            where = where.and(UserSpecification.nomeEquals(nome));
        } else if (nonNull(nome) && !nome.isBlank()) {
            where = UserSpecification.nomeEquals(nome);
        }

        if (nonNull(email) && !email.isBlank() && nonNull(where)) {
            where = where.and(UserSpecification.emailEquals(email));
        } else if (nonNull(email) && !email.isBlank()) {
            where = UserSpecification.emailEquals(email);
        }

        if (nonNull(dataInicial) && nonNull(where)) {
            if (nonNull(dataFinal)) {
                where = where.and(UserSpecification.nascimentoBetween(dataInicial, dataFinal));
            } else {
                where = where.and(UserSpecification.nascimentoEquals(dataInicial));
            }
        } else if (nonNull(dataInicial)) {
            if (nonNull(dataFinal)) {
                where = UserSpecification.nascimentoBetween(dataInicial, dataFinal);
            } else {
                where = UserSpecification.nascimentoEquals(dataInicial);
            }
        }
        return where;
    }

}