package com.myproject.twitter.dto.request;

import jakarta.validation.constraints.Size;

public record UserPatchRequestDto(

        @Size(max = 150 )
        String password,

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
