package com.nhc.JobHunter.controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhc.JobHunter.domain.Company;
import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.dto.ResultPaginationDTO;
import com.nhc.JobHunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> addCompanies(
            @Valid @RequestBody Company company) {

        Company newCompany = this.companyService.addCompany(company);

        return ResponseEntity.ok().body(newCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getListCompany(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        ResultPaginationDTO companies = this.companyService.getAllCompanies(spec, pageable);
        return ResponseEntity.ok().body(companies);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@RequestBody Company Company) {
        Company updateCompany = this.companyService.handleUpdateCompany(Company);
        return ResponseEntity.ok().body(updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok().body(null);
    }
}
