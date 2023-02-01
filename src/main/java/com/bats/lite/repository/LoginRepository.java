package com.bats.lite.repository;

import com.bats.lite.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepository extends JpaRepository<Login, UUID> {

    @Query("from Login l where l.email=:email")
    Optional<Login> findByUsername(String email);
}
