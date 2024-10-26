package com.nhc.JobHunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import com.nhc.JobHunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;
    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean isActive;

    private List<String> skills;

    private Instant updatedAt;
    private String updatedBy;
}
