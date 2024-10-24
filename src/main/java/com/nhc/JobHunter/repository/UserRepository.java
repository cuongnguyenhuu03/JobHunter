package com.nhc.JobHunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nhc.JobHunter.domain.Company;
import com.nhc.JobHunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String username);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String Email);

    List<User> findByCompany(Company company);
}
