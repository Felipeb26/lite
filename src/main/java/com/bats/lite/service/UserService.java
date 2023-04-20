package com.bats.lite.service;

import com.bats.lite.dto.UserDTO;
import com.bats.lite.entity.PageDTO;
import com.bats.lite.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<UserDTO> findByParam(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal);

    PageDTO findByParamPaged(Long id, String nome, String email, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable);

    UserDTO save(UserDTO user);

    UserDTO update(Long id, UserDTO user);

    String delete(Long id);


}