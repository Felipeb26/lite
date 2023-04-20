package com.bats.lite.service;

import com.bats.lite.dto.BancoDTO;
import com.bats.lite.entity.Banco;
import com.bats.lite.entity.PageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BancoService {

    PageDTO findPaginate(Long id, String nome, Long agencia, Long conta, Pageable pageable);

    List<BancoDTO> findBank(Long id, String nome, Long agencia, Long conta);

    Banco saveBanco(BancoDTO banco);

    Banco updateBanco(Long id, Banco banco);

    String deleteBanco(Long id);

}
