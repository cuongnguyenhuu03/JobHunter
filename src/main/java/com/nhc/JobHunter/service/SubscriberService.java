package com.nhc.JobHunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.Job;
import com.nhc.JobHunter.domain.Skill;
import com.nhc.JobHunter.domain.Subscriber;
import com.nhc.JobHunter.domain.response.email.ResEmailJob;
import com.nhc.JobHunter.repository.JobRepository;
import com.nhc.JobHunter.repository.SkillRepository;
import com.nhc.JobHunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(
            SkillRepository skillRepository,
            SubscriberRepository subscriberRepository,
            JobRepository jobRepository,
            EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

}
