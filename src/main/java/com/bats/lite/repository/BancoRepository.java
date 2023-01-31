package com.bats.lite.repository;

import com.bats.lite.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BancoRepository extends JpaRepository<Banco, Long>, JpaSpecificationExecutor<Banco> {
}
