package ru.afishaBMSTU.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import ru.afishaBMSTU.dto.gptResponse.GptResponseDto;
import ru.afishaBMSTU.utility.GPTPrompts;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class F5AIClient {

    private final WebClient f5AIWebClient;

    @Value("${integration.f5-ai.url}")
    private String f5AIUrl;

    public String getImageDescription(MultipartFile multipartFile) throws IOException {

        String base64Image = Base64.getEncoder().encodeToString(multipartFile.getBytes());
        String imageUrl = "data:image/jpeg;base64," + base64Image;

        String requestBody = String.format(GPTPrompts.IMAGE_DESCRIPTION_PROMPT, GPTPrompts.SYSTEM_CONTENT, imageUrl);

        return f5AIWebClient.post()
                .uri(f5AIUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GptResponseDto.class)
                .map(response -> response.getChoices().getFirst().getMessage().getContent())
                .block();
    }
}
