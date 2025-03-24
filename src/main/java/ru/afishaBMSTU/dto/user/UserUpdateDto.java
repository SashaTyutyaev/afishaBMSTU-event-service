package ru.afishaBMSTU.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Pattern(regexp = "^7\\d{10}$")
    private String phone;

    @Email
    private String email;
}
