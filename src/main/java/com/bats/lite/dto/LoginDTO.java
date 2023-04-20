package com.bats.lite.dto;

import com.bats.lite.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String senha;
    private String email;
    protected String dataCadastro;
    private Roles roles;

}
