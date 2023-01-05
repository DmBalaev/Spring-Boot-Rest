package com.dm.rest.init;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.RoleRepository;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class DataLoader {
    @Value("${default.admin.password}")
    private String adminPassword;
    @Value("${default.admin.mail}")
    private String adminEmail;

    @Bean
    public CommandLineRunner dataLoad(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder){
        return args -> {
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_USER"));
            log.info("Created USER and ADMIN roles");

            Collection<Role> roles = roleRepository.findAll();
            User admin = User.builder()
                    .email(adminEmail)
                    .password(encoder.encode(adminPassword))
                    .roles(roles)
                    .build();

            userRepository.save(admin);
            log.info("Created SUPER USER");
        };
    }
}
