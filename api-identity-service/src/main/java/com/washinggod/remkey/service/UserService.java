package com.washinggod.remkey.service;

import com.washinggod.remkey.configuration.properties.OtpConfig;
import com.washinggod.remkey.dto.request.*;
import com.washinggod.remkey.dto.response.UserResponse;
import com.washinggod.remkey.entity.Role;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.UserMapper;
import com.washinggod.remkey.repository.RoleRepository;
import com.washinggod.remkey.repository.UserRepository;
import com.washinggod.remkey.util.OtpService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    RedisTemplate<String, String> redisTemplate;

    OtpConfig otpConfig;

    OtpService otpService;

    public void create(UserCreationRequest request) {

        String usernameKey = "user:username:" + request.getEmail();
        String passwordKey = "user:password:" + request.getEmail();

        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTED);

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);

        redisTemplate.opsForValue().set(usernameKey, request.getUsername(), Duration.ofMinutes(otpConfig.getValidDuration()));
        redisTemplate.opsForValue().set(passwordKey, request.getPassword(), Duration.ofMinutes(otpConfig.getValidDuration()));
        try {
            otpService.generateOtpCode(request.getEmail());
        } catch (MessagingException e) {
            log.error("ERROR: Failed in sending verify email");
        }

    }


    public UserResponse verifyUserCreationRequest(VerifyUserCreationRequest request) {

        if (!otpService.verifyOtp(request.getCode(), request.getEmail())) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        String usernameKey = "user:username:" + request.getEmail();
        String passwordKey = "user:password:" + request.getEmail();

        String username = redisTemplate.opsForValue().get(usernameKey);
        String password = redisTemplate.opsForValue().get(passwordKey);

        redisTemplate.delete(usernameKey);
        redisTemplate.delete(passwordKey);

        User user = User.builder()
                .email(request.getEmail())
                .username(username)
                .password(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now())
                .build();

        Role role = roleRepository.findByName("USER").orElseThrow(() -> {
            return new AppException(ErrorCode.ROLE_NOT_EXIST);
        });

        Set<Role> roles = new HashSet<>();
        roles.add(role);
//
        user.setRoles(roles);


        return this.saveUser(user);

    }


    @PreAuthorize("#id == authentication.name || hasRole('ADMIN')")
    public UserResponse update(String id, UserUpdateRequest request) {

        User user = this.getUserById(id);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        if (user.getUsername().equals(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTED);

        if (user.getEmail().equals(request.getEmail())) throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);

        userMapper.updateUser(user, request);

        return this.saveUser(user);

    }

    @PreAuthorize("#id == authentication.name || hasRole('ADMIN')")
    public UserResponse changePassword(String id, ChangePasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        User user = this.getUserById(id);

        if (!passwordEncoder.matches(request.getCurrPassword(), user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return this.saveUser(user);
    }


    public UserResponse getMyInfo(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = this.getUserById(userId);
        return this.generateUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse provideAdminRole(String id) {
        User user = this.getUserById(id);

        Role role = roleRepository.findByName("ADMIN").orElseThrow(() -> {
            return new AppException(ErrorCode.ROLE_NOT_EXIST);
        });

        user.getRoles().add(role);

        return this.saveUser(user);
    }

    @PreAuthorize("#id == authentication.name || hasRole('ADMIN')")
    public UserResponse getUserResponseById(String id) {
        return this.generateUserResponse(this.getUserById(id));
    }

    
    public UserResponse getUserResponseByUsername(FindUserByUsernameRequest request) {
        return this.generateUserResponse(this.getUserByUsername(request.getUsername()));
    }


    public UserResponse getUserResponseByEmail(FindUserByEmailRequest request) {
        return this.generateUserResponse(this.getUserByEmail(request.getEmail()));
    }

    @PreAuthorize("#id == authentication.name || hasRole('ADMIN')")
    public void delete(String id) {

        userRepository.delete(this.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(this::generateUserResponse).toList();
    }


    private UserResponse saveUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        try {
            return this.generateUserResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException exception) {
            log.error("ERROR: save user failed because: {}", exception.getMessage());
            throw new AppException(ErrorCode.SAVE_USER_FAILED);
        }
    }

    private User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.USER_NOT_EXIST);
        });
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            return new AppException(ErrorCode.USER_NOT_EXIST);
        });
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            return new AppException(ErrorCode.USER_NOT_EXIST);
        });
    }


    private UserResponse generateUserResponse(User user) {

        UserResponse userResponse = userMapper.toUserResponse(user);

        if (user.getRoles() != null) {
            List<String> roles = user.getRoles().stream().map(Role::getName).toList();
            userResponse.setRoles(roles);
        }

        return userResponse;
    }

}
