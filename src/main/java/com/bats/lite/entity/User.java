package com.bats.lite.entity;

import com.bats.lite.enums.Roles;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@ToString
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
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<Banco> bancos;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "loginId")
    private Login login;
}
