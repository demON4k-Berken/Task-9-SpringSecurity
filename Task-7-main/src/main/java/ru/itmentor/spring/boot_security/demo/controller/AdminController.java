package ru.itmentor.spring.boot_security.demo.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String findAllUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);
        return "/admin";
    }

    @GetMapping("/saveUser")
    public String saveUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "/saveUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           @RequestParam("roles") Set<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            return "/saveUser";
        }

        Set<Role> roles = roleIds.stream()
                .map(id -> roleRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/updateUser")
    public String updateUserForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "/updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam("roles") Set<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            return "/updateUser";
        }
        Set<Role> roles = roleIds.stream()
                .map(id -> roleRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
