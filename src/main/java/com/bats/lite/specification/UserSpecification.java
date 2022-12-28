package com.bats.lite.specification;

import com.bats.lite.entity.User;
import com.bats.lite.entity.User_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@UtilityClass
public class UserSpecification {

    public Specification<User> exists() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(User_.ID), 0);
    }

    public static Specification<User> iDEguals(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.ID), id);
    }

    public static Specification<User> nomeEquals(String nome) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.NOME), nome);
    }

    public static Specification<User> emailEquals(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.EMAIL), email);
    }

    public static Specification<User> nascimentoEquals(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.NASCIMENTO), date);
    }

    public static Specification<User> nascimentoBetween(LocalDate dataInicial, LocalDate dataFinal) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get(User_.NASCIMENTO), dataInicial, dataFinal);
    }
}
