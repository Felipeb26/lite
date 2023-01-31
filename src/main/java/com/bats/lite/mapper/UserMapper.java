package com.bats.lite.mapper;

import com.bats.lite.dto.UserDTO;
import com.bats.lite.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public User toEntity(UserDTO dto) {
		return User.builder()
			.nome(dto.getNome())
			.nascimento(dto.getNascimento())
			.email(dto.getEmail())
			.senha(dto.getSenha())
			.roles(dto.getRoles())
			.build();
	}

	public UserDTO toDTO(User entity) {
		return UserDTO.builder()
			.nome(entity.getNome())
			.nascimento(entity.getNascimento())
			.email(entity.getEmail())
			.senha(entity.getSenha())
			.roles(entity.getRoles())
			.build();
	}

}
