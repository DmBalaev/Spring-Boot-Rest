package com.dm.rest.init;

import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.repository.RoleRepository;
import com.dm.rest.persistance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader {

    @Bean
    public CommandLineRunner dataLoad(RoleRepository roleRepository, UserRepository userRepository){
        return args -> {
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_USER"));

            log.info("Created USER and ADMIN roles");
        };
    }
}
