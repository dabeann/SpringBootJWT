package jwtjwt.jwtTutorial.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    // 로그인 정보를 담을 Dto

    @NotNull // Validation 어노테이션
    @Size(min = 3, max = 50) // Validation 어노테이션
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}