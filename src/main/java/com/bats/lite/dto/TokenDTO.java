package com.bats.lite.dto;

import lombok.*;

import java.io.Serializable;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO implements Serializable {

    private Object time;
    private String type;
    private String token;
}
