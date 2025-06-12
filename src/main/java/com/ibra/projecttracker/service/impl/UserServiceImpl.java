package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.exception.InvalidCredentialException;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, EntityDTOMapper entityDTOMapper, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDTO createUser(UserCreateRequest userCreateRequest) {
        User newUser = User.builder()
                .firstName(userCreateRequest.getFirstName())
                .lastName(userCreateRequest.getLastName())
                .email(userCreateRequest.getEmail())
//                .password(userCreateRequest.getPassword())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                .phoneNumber(userCreateRequest.getPhoneNumber())
                .userRole(userCreateRequest.getUserRole())
                .build();
        //CAN I CONTROL AN ERROR MESSAGE
        User savedUser = userRepository.save(newUser);
        return entityDTOMapper.mapUserToUserDTO(savedUser);
    }

    @Override
    public String loginUser(AuthRequest authRequest) {
        log.info("Attempting to login user with email: {}", authRequest.email());
        User loginUser = userRepository.findByEmail(authRequest.email())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        log.info("Found email of the loginUser: {}", loginUser.getEmail());
        if(!passwordEncoder.matches(authRequest.password(), loginUser.getPassword())) {
            throw new InvalidCredentialException("Wrong password");
        }

        log.debug("loginUser: {}", loginUser);

        return jwtUtils.generateToken(authRequest.email());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(entityDTOMapper::mapUserToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return entityDTOMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if (userDTO.firstName() != null) user.setFirstName(userDTO.firstName());
        if (userDTO.lastName() != null) user.setLastName(userDTO.lastName());
        if (userDTO.email() != null) user.setEmail(userDTO.email());
        if (userDTO.phoneNumber() != null) user.setPhoneNumber(userDTO.phoneNumber());

        User updatedUser = userRepository.save(user);

        return entityDTOMapper.mapUserToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }
}
