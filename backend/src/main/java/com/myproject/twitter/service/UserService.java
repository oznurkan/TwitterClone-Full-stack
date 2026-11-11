package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.UserPatchRequestDto;
import com.myproject.twitter.dto.response.UserProfileResponseDto;
import com.myproject.twitter.dto.response.UserResponseDto;
import com.myproject.twitter.entity.User;

public interface UserService {

    UserResponseDto getUserByEmail();

    UserProfileResponseDto getUserByNickName(String nickName);

    UserResponseDto updateProfile(UserPatchRequestDto userPatchRequestDto);

    User getEntityByEmail(String email);

    User getEntityByNickName(String nickName);


}
