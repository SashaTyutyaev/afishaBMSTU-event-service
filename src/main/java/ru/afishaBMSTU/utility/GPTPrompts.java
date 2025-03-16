package ru.afishaBMSTU.utility;

import lombok.Data;

@Data
public class GPTPrompts {

    public static final String IMAGE_DESCRIPTION_PROMPT = """
             {
                 "model": "gpt-4o-mini",
                 "max_tokens": 16383,
                 "messages": [
                     {
                         "role": "system",
                         "content": "%s"
                     },
                     {
                         "role": "user",
                         "content": [
                             {
                                 "type": "image_url",
                                 "image_url": {
                                     "url": "%s"
                                 }
                             }
                         ]
                     }
                 ]
             }
            """;

    public static final String SYSTEM_CONTENT = "Ты — помощник, который максимально красочно и подробно описывает" +
            " изображения для слабовидящих людей. На русском языке. Ты описываешь изображения картинки мероприятия с афиши. " +
            "Опиши так чтобы слабовидящий человек был воодушевлен и заинтересован мероприятием. Попробуй завлечь его туда.";
}
