package com.myproject.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TweetPatchRequestDto(

        @NotBlank(message = "Tweet içeriği boş bırakılamaz.")
        @Size(max = 255)
        String content
) {
}
