package ru.afishaBMSTU.dto.bmstu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private String slug;
    private String title;
    private String imagePreview;
    private List<TagDto> tags;

    @JsonProperty("page_url")
    private String pageUrl;
}
