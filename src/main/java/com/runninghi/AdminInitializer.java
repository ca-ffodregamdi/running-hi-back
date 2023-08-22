package com.runninghi;

import com.runninghi.user.command.domain.aggregate.entity.User;
import com.runninghi.user.command.domain.aggregate.entity.enumtype.Role;
import com.runninghi.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
//        System.out.println(RandomStringUtils.randomAlphanumeric(128));
        userRepository.save(User.builder()
                .account("admin")
                .password(encoder.encode("admin"))
                .name("관리자")
                .nickname("자리관")
                .email("admin@admin.kr")
                .role(Role.ADMIN)
                .build());
    }
}
