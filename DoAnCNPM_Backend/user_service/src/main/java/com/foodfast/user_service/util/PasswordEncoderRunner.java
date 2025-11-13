package com.foodfast.user_service.util;

import com.foodfast.user_service.model.User;
import com.foodfast.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordEncoderRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.findAll();
        int updated = 0;

        for (User user : users) {
            String password = user.getPassword();

            // Kiểm tra nếu chưa encode (BCrypt bắt đầu bằng $2a$ hoặc $2b$)
            if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                updated++;
            }
        }

        System.out.println("✅ Passwords updated: " + updated);
    }
}
