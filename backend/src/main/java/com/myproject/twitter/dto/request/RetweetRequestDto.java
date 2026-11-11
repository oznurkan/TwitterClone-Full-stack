package com.myproject.twitter.dto.request;

import jakarta.validation.constraints.Size;

public record RetweetRequestDto(

        @Size( max = 255, message = "Yorum en fazla 255 karakter olabilir." )
        String text

) {
}
