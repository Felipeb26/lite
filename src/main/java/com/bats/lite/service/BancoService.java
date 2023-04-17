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

public interface BancoService {

    PageDTO findPaginate(Long id, String nome, Long agencia, Long conta, Pageable pageable);

    List<BancoDTO> findBank(Long id, String nome, Long agencia, Long conta);

    Banco saveBanco(BancoDTO banco);

    Banco updateBanco(Long id, Banco banco);

    String deleteBanco(Long id);

}
