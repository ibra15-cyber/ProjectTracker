package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.entity.*;
import com.ibra.projecttracker.exception.InvalidCredentialException;
import com.ibra.projecttracker.exception.InvalidTokenException;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.*;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final DeveloperRepository developerRepository;
    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;
    private final ContractorRepository contractorRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, EntityDTOMapper entityDTOMapper, JwtUtils jwtUtils, AuditLogService auditLogService, DeveloperRepository developerRepository, AdminRepository adminRepository, DeveloperRepository developerRepository1, AdminRepository adminRepository1, ManagerRepository managerRepository, ContractorRepository contractorRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.jwtUtils = jwtUtils;
        this.auditLogService = auditLogService;
        this.developerRepository = developerRepository1;
        this.adminRepository = adminRepository1;
        this.managerRepository = managerRepository;
        this.contractorRepository = contractorRepository;
    }

    @Override
    public UserDTO createUser(UserCreateRequest userCreateRequest) {
        User newUser = User.builder()
                .firstName(userCreateRequest.getFirstName())
                .lastName(userCreateRequest.getLastName())
                .email(userCreateRequest.getEmail())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                .phoneNumber(userCreateRequest.getPhoneNumber())
                .userRole(userCreateRequest.getUserRole())
                .build();
        //CAN I CONTROL AN ERROR MESSAGE
        User savedUser = userRepository.save(newUser);
        if (savedUser.getUserRole().name().equals("DEVELOPER")) {
            Developer developer = new Developer();
            developer.setDeveloperId(savedUser.getUserId());
            developer.setUser(savedUser);
            developer.setName(userCreateRequest.getFirstName() + " " + userCreateRequest.getLastName());
            developer.setEmail(userCreateRequest.getEmail());
            developer.setSkills(userCreateRequest.getDevSkills());

            developerRepository.save(developer);
        } else if (savedUser.getUserRole().name().equals("ADMIN")) {
            Admin admin = new Admin();
            admin.setAdminId(savedUser.getUserId());
            admin.setUser(savedUser);
            adminRepository.save(admin);
        } else if (savedUser.getUserRole().name().equals("MANAGER")) {
            Manager manager = new Manager();
            manager.setMangerId(savedUser.getUserId());
            manager.setUser(savedUser);
            managerRepository.save(manager);
        } else {
            Contractor contractor = new Contractor();
            contractor.setContractorId(savedUser.getUserId());
            contractor.setUser(savedUser);
            contractorRepository.save(contractor);
        }

        return entityDTOMapper.mapUserToUserDTO(savedUser);
    }

    @Override
    public Map<String, String> loginUser(AuthRequest authRequest) {
        log.info("Attempting to login user with email: {}", authRequest.email());
        User loginUser = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> {
                    auditLogService.saveAuditLog(AuditLog.loginFailedLog(authRequest.email(), "User not found"));
                    return new ResourceNotFoundException("User not found");
                });

        log.info("Found email of the loginUser: {}", loginUser.getEmail());

        if (!passwordEncoder.matches(authRequest.password(), loginUser.getPassword())) {
            auditLogService.saveAuditLog(AuditLog.loginFailedLog(authRequest.email(), "Invalid password"));
            throw new InvalidCredentialException("Wrong password");
        }

        log.debug("loginUser: {}", loginUser);
        String token = jwtUtils.generateToken(authRequest.email());
        String refreshToken = jwtUtils.generateRefreshToken(authRequest.email());

        Map<String, Long> accessTokenTime = jwtUtils.getTokenTimeDetails(token);
        Map<String, Long> refreshTokenTime = jwtUtils.getTokenTimeDetails(refreshToken);

        loginUser.setRefreshedToken(refreshToken);
        userRepository.save(loginUser);

        return Map.of(
                "accessToken", token,
                "refreshToken", refreshToken,
                "accessTokenExpiresIn", String.valueOf(accessTokenTime.get("remainingTimeMs") / 1000),
                "refreshTokenExpiresIn", String.valueOf(refreshTokenTime.get("remainingTimeMs") / 1000),
                "userRole", loginUser.getUserRole().name(),
                "email", loginUser.getEmail()
        );


    }


    @Override
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new InvalidTokenException("Refresh token must be provided in Authorization header with Bearer prefix");
        }

        String refreshToken = bearerToken.substring(7);

        String email = jwtUtils.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!refreshToken.equals(user.getRefreshedToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String newAccessToken = jwtUtils.generateToken(user.getEmail());
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        Map<String, Long> accessTokenTime = jwtUtils.getTokenTimeDetails(newAccessToken);
        Map<String, Long> refreshTokenTime = jwtUtils.getTokenTimeDetails(newRefreshToken);

        user.setRefreshedToken(newRefreshToken);
        userRepository.save(user);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken,
                "accessTokenExpiresIn", String.valueOf(accessTokenTime.get("remainingTimeMs") / 1000),
                "refreshTokenExpiresIn", String.valueOf(refreshTokenTime.get("remainingTimeMs") / 1000),
                "userRole", user.getUserRole().name()
        );

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityDTOMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserDTO getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User email is: {}", email);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return entityDTOMapper.mapUserToUserDTO(loggedInUser);
    }
}