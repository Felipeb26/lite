package com.bats.lite.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@ToString
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Banco extends AbstractEntity<Banco> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private Long conta;
    private Long agencia;
    private int debito;
    private int credito;
    private int poupanca;
    private int emprestimo;

}
