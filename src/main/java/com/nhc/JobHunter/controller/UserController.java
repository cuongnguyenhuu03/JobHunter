package com.nhc.JobHunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return this.userService.getAllUser();
    }

    @GetMapping("/user/{id}")
    public User getUserById(
            @PathVariable long id) {
        return this.userService.getUserById(id);
    }

    @PostMapping("/user")
    public User createNewUser(
            @RequestBody User newUser) {

        User myUser = this.userService.handleCreateUser(newUser);
        return myUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(
            @PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return "delete";
    }

    @PutMapping("/user")
    public User updateUser(
            @RequestBody User newUser) {
        return this.userService.handleUpdateUser(newUser);
    }
}
