package ru.afishaBMSTU.dto.gptResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDto {
    private MessageDto message;
}
