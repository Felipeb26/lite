package com.bats.lite.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity<User> {

    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    private String senha;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nascimento;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<Banco> bancos;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loginId")
    private Login login;
}
