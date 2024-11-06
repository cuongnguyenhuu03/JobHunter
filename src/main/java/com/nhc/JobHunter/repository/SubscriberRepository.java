package com.nhc.JobHunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nhc.JobHunter.domain.Subscriber;

@Repository
public interface SubscriberRepository extends
        JpaRepository<Subscriber, Long>,
        JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);
}
