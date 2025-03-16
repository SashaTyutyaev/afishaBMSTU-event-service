package ru.afishaBMSTU.users.events.model.dto.gptResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptResponseDto {
    List<ChoiceDto> choices;
}
