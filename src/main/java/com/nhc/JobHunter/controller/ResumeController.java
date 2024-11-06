package com.nhc.JobHunter.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhc.JobHunter.domain.Company;
import com.nhc.JobHunter.domain.Job;
import com.nhc.JobHunter.domain.Resume;
import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.response.ResultPaginationDTO;
import com.nhc.JobHunter.domain.response.resume.ResCreateResumeDTO;
import com.nhc.JobHunter.domain.response.resume.ResFetchResumeDTO;
import com.nhc.JobHunter.domain.response.resume.ResUpdateResumeDTO;
import com.nhc.JobHunter.service.ResumeService;
import com.nhc.JobHunter.service.UserService;
import com.nhc.JobHunter.util.SecurityUtil;
import com.nhc.JobHunter.util.anotation.ApiMessage;
import com.nhc.JobHunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(
            ResumeService resumeService,
            UserService userService,
            FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("resumes")
    @ApiMessage("create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume)
            throws IdInvalidException {
        boolean isExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isExist) {
            throw new IdInvalidException("Job/User not exist");
        }
        return ResponseEntity.ok().body(this.resumeService.createResume(resume));
    }

    @GetMapping("resumes/{id}")
    @ApiMessage("get a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchResume(@PathVariable Long id) throws IdInvalidException {
        Optional<Resume> optionalResume = this.resumeService.fetchResume(id);
        if (optionalResume.isEmpty()) {
            throw new IdInvalidException("resume with id: " + id + " not exist");
        } else {
            return ResponseEntity.ok().body(this.resumeService.getResume(optionalResume.get()));
        }
    }

    @PutMapping("resumes")
    @ApiMessage("update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> optionalResume = this.resumeService.fetchResume(resume.getId());
        if (optionalResume.isEmpty()) {
            throw new IdInvalidException("resume with id: " + resume.getId() + " not exist");
        } else {
            Resume updateResume = optionalResume.get();
            updateResume.setStatus(resume.getStatus());
            return ResponseEntity.ok().body(this.resumeService.update(updateResume));
        }
    }

    @DeleteMapping("resumes/{id}")
    @ApiMessage("delete a resume with id")
    public void deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> optionalResume = this.resumeService.fetchResume(id);
        if (optionalResume.isEmpty()) {
            throw new IdInvalidException("resume with id: " + id + " not exist");
        } else {
            this.resumeService.deleteResume(id);
        }
    }

    @GetMapping("resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        List<Long> arrJobIds = null;

        // get user name
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleGetUserByUserName(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(pageable, finalSpec));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }

}