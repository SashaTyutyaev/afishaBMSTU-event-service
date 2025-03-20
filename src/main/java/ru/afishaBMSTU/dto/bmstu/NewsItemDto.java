package ru.afishaBMSTU.dto.bmstu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsItemDto extends ItemDto {
    @JsonProperty("published_at")
    private PublishedAtDto publishedAt;

    @JsonProperty("preview_text")
    private String previewText;
}
