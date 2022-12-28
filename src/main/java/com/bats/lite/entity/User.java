package com.bats.lite.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity<User> {

    @Id
    private Long id;
    private String nome;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nascimento;
}
