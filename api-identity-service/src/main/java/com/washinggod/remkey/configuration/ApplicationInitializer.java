package com.washinggod.remkey.configuration;

import com.washinggod.remkey.entity.Role;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.RoleRepository;
import com.washinggod.remkey.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@Component
public class ApplicationInitializer implements ApplicationRunner {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userRepository.findByUsername("admin").ifPresentOrElse(userExisted -> {
            log.info("User admin has been created");
        }, () -> {
            Role role = roleRepository.findByName("ADMIN").orElseThrow(() -> {
                return new AppException(ErrorCode.ROLE_NOT_EXIST);
            });
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("12345678"))
                    .email("remkey@gmail.com")
                    .roles(roles)
                    .build();

            userRepository.save(user);

            log.info("INFO: Create user admin with username: admin and password: 12345678");
        });

    }
}