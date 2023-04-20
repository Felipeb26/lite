package com.bats.lite.mapper;

import com.bats.lite.dto.LoginDTO;
import com.bats.lite.entity.Login;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class LoginMapper {

    public static List<Login> dtosTologins(List<LoginDTO> loginDTOS) {
        return loginDTOS.stream().map(LoginMapper::dtoToLogin).collect(Collectors.toList());

    }
    public static List<LoginDTO> entityToDTO(List<Login> login) {
        return login.stream().map(LoginMapper::entitytoDTO).collect(Collectors.toList());
    }

    public static Login dtoToLogin(LoginDTO dto) {
        return Login.builder()
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .dataCadastro(dto.getDataCadastro())
                .build();
    }

    public static LoginDTO entitytoDTO(Login entity) {
        return LoginDTO.builder()
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }

}
