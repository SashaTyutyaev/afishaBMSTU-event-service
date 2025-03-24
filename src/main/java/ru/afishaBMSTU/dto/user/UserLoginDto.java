package ru.afishaBMSTU.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @NotNull
    @NotEmpty
    @NotBlank
    private String nickname;

    @Size(min = 8, max = 500)
    @NotNull
    @NotEmpty
    @NotBlank
    private String password;
}