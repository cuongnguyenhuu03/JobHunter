package com.nhc.JobHunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.response.ResCreateUserDTO;
import com.nhc.JobHunter.domain.response.ResUpdateUserDTO;
import com.nhc.JobHunter.domain.response.ResUserDTO;
import com.nhc.JobHunter.domain.response.ResultPaginationDTO;
import com.nhc.JobHunter.service.UserService;
import com.nhc.JobHunter.util.anotation.ApiMessage;
import com.nhc.JobHunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        ResultPaginationDTO list = this.userService.getAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(
            @PathVariable long id) throws IdInvalidException {
        User fetchUser = this.userService.getUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToResUserDTO(fetchUser));
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(
            @Valid @RequestBody User newUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(newUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + newUser.getEmail() + " already exists, please use another email.");
        }
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        User createdUser = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createdUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Object> deleteUser(
            @PathVariable long id) throws IdInvalidException {

        User currentUser = this.userService.getUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("user does not exist");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(
            @RequestBody User newUser) throws IdInvalidException {
        User updatedUser = this.userService.handleUpdateUser(newUser);
        if (updatedUser == null) {
            throw new IdInvalidException("User does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(updatedUser));
    }

}
