package github.migueldelgg.springsecurity.controller;

import github.migueldelgg.springsecurity.controller.dto.CreateUserDTO;
import github.migueldelgg.springsecurity.entities.Role;
import github.migueldelgg.springsecurity.entities.User;
import github.migueldelgg.springsecurity.repositories.RoleRepository;
import github.migueldelgg.springsecurity.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/users")
    @Transactional
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO dto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb= userRepository.findByUsername(dto.username());

        if(userFromDb.isPresent()) {
            System.out.println("User already exists");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
