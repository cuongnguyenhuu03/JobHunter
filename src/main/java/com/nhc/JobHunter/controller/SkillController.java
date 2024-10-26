package com.nhc.JobHunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhc.JobHunter.domain.Skill;
import com.nhc.JobHunter.domain.response.ResultPaginationDTO;
import com.nhc.JobHunter.service.SkillService;
import com.nhc.JobHunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        // check skill exist
        if (skill.getName() != null && this.skillService.checkExistsName(skill.getName())) {
            throw new IdInvalidException(skill.getName() + " already exists");
        }
        return ResponseEntity.ok().body(this.skillService.addSkill(skill));
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (skill.getName() != null && this.skillService.checkExistsName(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " already exists ");
        }

        Skill updateSkill = this.skillService.updateSkill(skill);
        if (updateSkill == null) {
            throw new IdInvalidException("Skill does not exist");
        }
        return ResponseEntity.ok().body(updateSkill);
    }

    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.skillService.getAllSkill(spec, pageable));
    }

}
