package com.bats.lite.mapper;

import com.bats.lite.dto.UserDTO;
import com.bats.lite.entity.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static List<User> toListEntity(List<UserDTO> usersDtos) {
        return usersDtos.stream().map(UserMapper::toEntity).collect(Collectors.toList());
    }

    public static List<UserDTO> toListDTO(List<User> users) {
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .nome(dto.getNome())
                .nascimento(dto.getNascimento())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .bancos(BancoMapper.dtosParaBancos(dto.getBancos()))
                .login(LoginMapper.dtoToLogin(dto.getLogin()))
                .build();
    }

    public static UserDTO toDTO(User entity) {
        return UserDTO.builder()
                .nome(entity.getNome())
                .nascimento(entity.getNascimento())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .bancos(BancoMapper.bancosParaDTOS(entity.getBancos()))
                .login(LoginMapper.entitytoDTO(entity.getLogin()))
                .build();
    }

}
