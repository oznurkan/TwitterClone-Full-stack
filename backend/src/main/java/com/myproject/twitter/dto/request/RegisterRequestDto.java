package com.myproject.twitter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @Email
        @NotBlank(message = "Email alanı boş bırakılamaz.")
        @Size(max = 255 )
        String email,

        @NotBlank(message = "Şifre alanı boş bırakılamaz")
        @Size(max = 150, min = 6 )
        String password,

        @NotBlank(message = "Kullanıcı ismi boş bırakılamaz.")
        @Size(max = 150 , min = 3)
        String nickName,

        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Size(max = 255 )
        String bio

) {
}
