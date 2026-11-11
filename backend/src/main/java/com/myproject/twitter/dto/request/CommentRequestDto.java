package com.myproject.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(

        @Size(max = 255)
        @NotBlank(message = "Yorum içeriği boş bırakılamaz.")
        String content

){
}
