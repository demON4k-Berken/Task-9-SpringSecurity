package ru.itmentor.spring.boot_security.demo.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/admin/")
public class AdminController {                              //finish

    private final UserService userService;
    private final RoleRepository roleRepository;        //add

//    public AdminController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping
    public String findAllUsers(Model model) {
        List<User> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);
        return "/admin";
    }

    @GetMapping("/saveUser")
    public String saveUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());       //add
        return "/saveUser";
    }

    //    @PostMapping("/saveUser")
//    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "/saveUser";
//        }
//        userService.saveUser(user);
//        return "redirect:/admin/";
//    }
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
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleRepository.findAll());       //add
        return "/updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/updateUser";
        }
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
