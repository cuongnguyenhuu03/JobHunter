package com.nhc.JobHunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.dto.ResultPaginationDTO;
import com.nhc.JobHunter.service.UserService;
import com.nhc.JobHunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        ResultPaginationDTO list = this.userService.getAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(
            @RequestBody User newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        User myUser = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(myUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable long id) throws IdInvalidException {
        if (id > 100) {
            throw new IdInvalidException("errorr");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(
            @RequestBody User newUser) {
        User updatedUser = this.userService.handleUpdateUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
