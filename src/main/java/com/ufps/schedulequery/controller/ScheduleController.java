package com.ufps.schedulequery.controller;

import com.ufps.schedulequery.entities.VetDailySchedule;
import com.ufps.schedulequery.repositories.VetDailyScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final VetDailyScheduleRepository repo;

    public ScheduleController(VetDailyScheduleRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{vetId}")
    public List<VetDailySchedule> getSchedule(@PathVariable Long vetId) {
        return repo.findByVetId(vetId);
    }
}
