package com.nhc.JobHunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nhc.JobHunter.domain.Skill;
import com.nhc.JobHunter.domain.response.ResultPaginationDTO;
import com.nhc.JobHunter.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean checkExistsName(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill addSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill updateSkill(Skill skill) {
        Skill updateSkill = this.findById(skill.getId());
        if (updateSkill != null) {
            updateSkill.setName(skill.getName());
        }
        return this.skillRepository.save(updateSkill);
    }

    public Skill findById(long id) {
        return this.skillRepository.findById(id).get();
    }

    public ResultPaginationDTO getAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageUser = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }
}
