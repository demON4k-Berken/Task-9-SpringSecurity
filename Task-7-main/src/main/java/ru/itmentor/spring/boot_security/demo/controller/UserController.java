package ru.itmentor.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/")
public class UserController {                       // надо добавить пользователю базовые возможности в своей учетной записи
    private final UserService userService;

    @GetMapping
    public String userPage(@AuthenticationPrincipal User principal, Model model) {

        log.info("User page accessed by: {}", principal.getUsername());
        model.addAttribute("user", principal);
        return "user";
    }
}
