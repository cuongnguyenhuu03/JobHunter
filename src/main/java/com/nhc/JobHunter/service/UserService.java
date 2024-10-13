package com.nhc.JobHunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.dto.Meta;
import com.nhc.JobHunter.domain.dto.ResultPaginationDTO;
import com.nhc.JobHunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageUser.getContent());

        return rs;
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

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }
}
