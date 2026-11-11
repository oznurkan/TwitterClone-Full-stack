package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.UserPatchRequestDto;
import com.myproject.twitter.dto.response.UserProfileResponseDto;
import com.myproject.twitter.dto.response.UserResponseDto;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.UserNotFoundException;
import com.myproject.twitter.repository.UserRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AuthUtils authUtils;

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail() {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        String currentUserEmail = currentUser.getUsername();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserByNickName(String nickName) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        String currentUserEmail = currentUser.getUsername();

        return userRepository.getUserProfileDtoByUsername(nickName, currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + nickName));
    }


    @Override
    @Transactional
    public UserResponseDto updateProfile(UserPatchRequestDto userPatchRequestDto) {

        CustomUserDetails currentUser = authUtils.getRequiredCurrentUserDetails();

        String currentUserEmail = currentUser.getUsername();

        User userToUpdate = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        authUtils.verifyOwnership(userToUpdate.getId());

        userMapper.updateEntity(userToUpdate, userPatchRequestDto);

        userRepository.save(userToUpdate);

        return userMapper.toResponseDto(userToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public User getEntityByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public User getEntityByNickName(String nickName) {

        return userRepository.findByNickName(nickName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + nickName));
    }

}
