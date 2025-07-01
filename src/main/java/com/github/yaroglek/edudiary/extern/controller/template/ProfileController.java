package com.github.yaroglek.edudiary.extern.controller.template;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.User;
import com.github.yaroglek.edudiary.extern.dto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();

        UserDto dto = new UserDto();

        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        model.addAttribute("userDto", dto);
        return "profile";
    }
}

