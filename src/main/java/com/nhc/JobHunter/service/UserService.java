package com.nhc.JobHunter.service;

import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id).get();
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleUpdateUser(User user) {
        User updateUser = this.getUserById(user.getId());
        if (updateUser != null) {
            updateUser.setEmail(user.getEmail());
            updateUser.setName(user.getName());
            updateUser.setPassword(user.getPassword());

            updateUser = this.userRepository.save(updateUser);
        }
        return updateUser;
    }
}
