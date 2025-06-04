package ru.itmentor.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {                       // есть проблема с выводом актуальных данных после
                                                    // изменения в личном кабинете со страницы админа
    private final UserService userService;

    @GetMapping
    public String userPage(@AuthenticationPrincipal User principal, Model model) {
        model.addAttribute("user", principal);
        return "user";
    }
}
