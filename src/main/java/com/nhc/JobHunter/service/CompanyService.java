package com.nhc.JobHunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.Company;
import com.nhc.JobHunter.domain.User;
import com.nhc.JobHunter.domain.response.ResultPaginationDTO;
import com.nhc.JobHunter.repository.CompanyRepository;
import com.nhc.JobHunter.repository.UserRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(
            UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company addCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Optional<Company> findById(Long id) {
        return this.companyRepository.findById(id);
    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company handleUpdateCompany(Company company) {
        Company updateCompany = this.companyRepository.findById(company.getId()).get();
        if (updateCompany != null) {
            updateCompany.setName(company.getName());
            updateCompany.setAddress(company.getAddress());
            updateCompany.setDescription(company.getDescription());
            updateCompany.setLogo(company.getLogo());
            this.companyRepository.save(updateCompany);
        }
        return updateCompany;
    }

    public void handleDeleteCompany(Long id) {

        Optional<Company> comOptional = this.companyRepository.findById(id);
        if (comOptional.isPresent()) {
            Company com = comOptional.get();
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }

        this.companyRepository.deleteById(id);
    }
}
