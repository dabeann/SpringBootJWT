package jwtjwt.jwtTutorial.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {
    // Response 시 DTO를 통해서만 응답하도록

    private String authorityName;
}