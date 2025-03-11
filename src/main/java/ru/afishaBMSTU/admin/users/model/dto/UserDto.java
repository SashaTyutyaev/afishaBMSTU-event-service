package ru.afishaBMSTU.admin.users.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @Email
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
}
