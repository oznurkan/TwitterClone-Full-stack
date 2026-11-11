package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.RegisterRequestDto;
import com.myproject.twitter.dto.response.AuthResponseDto;
import com.myproject.twitter.dto.response.UserResponseDto;
import com.myproject.twitter.entity.Role;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.entity.enums.RoleType;
import com.myproject.twitter.exception.RoleNotFoundException;
import com.myproject.twitter.exception.UserAlreadyRegisteredException;
import com.myproject.twitter.repository.RoleRepository;
import com.myproject.twitter.repository.UserRepository;
import com.myproject.twitter.util.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthMapper authMapper;

    private final UserService userService;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {

        if (userRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new UserAlreadyRegisteredException("Email zaten kayıtlı!");
        }

        User user = authMapper.toEntity(registerRequestDto);
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));

        Role userRole = roleRepository.findByAuthority(RoleType.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER rolü bulunamadı!"));

        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        return authMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail() {

        return userService.getUserByEmail();
    }

}
