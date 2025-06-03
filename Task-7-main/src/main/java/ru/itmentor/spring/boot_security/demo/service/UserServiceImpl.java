package ru.itmentor.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initDefaultUsers() {
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        Role userRole = getOrCreateRole("ROLE_USER");

        createOrUpdateUser("admin", "admin", adminRole, userRole);
        createOrUpdateUser("user", "user", userRole);
    }

    private void createOrUpdateUser(String username, String rawPassword, Role... roles) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new User();
            user.setUsername(username);
        }
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(new HashSet<>(Set.of(roles)));
        userRepository.save(user);
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.findById(user.getId()).orElseThrow();
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }
}
