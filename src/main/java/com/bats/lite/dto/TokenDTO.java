package com.bats.lite.dto;

import lombok.*;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private Object time;
    private String type;
    private String token;
}
