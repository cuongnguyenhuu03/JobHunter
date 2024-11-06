package com.nhc.JobHunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.Skill;
import com.nhc.JobHunter.domain.Subscriber;
import com.nhc.JobHunter.repository.SkillRepository;
import com.nhc.JobHunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(
            SkillRepository skillRepository,
            SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Optional<Subscriber> findById(Long id) {
        return this.subscriberRepository.findById(id);
    }

    public Subscriber createSubscriber(Subscriber subs) {
        // check skill
        if (subs.getSkills() != null) {
            List<Long> skills = subs.getSkills()
                    .stream()
                    .map(item -> item.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skills);
            subs.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subs);
    }

    public boolean isExist(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber updateSubs(Subscriber subs, Subscriber updateSubscriber) {

        if (subs.getSkills() != null) {
            List<Long> skills = subs.getSkills()
                    .stream()
                    .map(item -> item.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skills);
            updateSubscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(updateSubscriber);
    }
}
