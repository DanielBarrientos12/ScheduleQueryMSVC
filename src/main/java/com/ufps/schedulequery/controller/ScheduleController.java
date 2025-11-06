package com.ufps.schedulequery.controller;

import com.ufps.schedulequery.entities.VetDailySchedule;
import com.ufps.schedulequery.repositories.VetDailyScheduleRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final VetDailyScheduleRepository repo;

    public ScheduleController(VetDailyScheduleRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Object getSchedule(@RequestParam Long vetId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VetDailySchedule> rows = repo.findByVetIdAndDateOrderBySlotTime(vetId, date);
        var items = rows.stream().map(r -> new java.util.HashMap<String,Object>() {{
            put("hour", r.getSlotTime() != null ? r.getSlotTime().toString().substring(0,5) : null);
            put("appointmentId", r.getAppointmentId());
            put("pet", r.getPetName());
            put("client", r.getClientName());
            put("reason", r.getReason());
            put("status", r.getStatus());
        }}).toList();

        return java.util.Map.of(
                "success", true,
                "vetId", vetId,
                "date", date.toString(),
                "timezone", "America/Bogota",
                "items", items
        );
    }
}
