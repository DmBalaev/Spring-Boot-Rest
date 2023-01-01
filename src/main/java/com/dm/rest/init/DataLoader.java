package com.dm.rest.init;

import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.RoleRepository;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataLoader {
    @Value("${default.admin.login}")
    private String adminLogin;
    @Value("${default.admin.password}")
    private String adminPassword;
    @Value("${default.admin.mail}")
    private String adminEmail;

    @Bean
    public CommandLineRunner dataLoad(RoleRepository roleRepository, UserRepository userRepository, UserService userService){
        return args -> {
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_USER"));

            log.info("Created USER and ADMIN roles");

            User admin = new User();
            admin.setLogin(adminLogin);
            admin.setPassword(adminPassword);
            admin.setEmail(adminEmail);

            userService.createUser(admin);
            userService.getAdminRoleInit(admin.getLogin());
        };
    }
}
