package com.ufps.schedulequery.service;

import com.ufps.schedulequery.dto.AppointmentEventDto;
import com.ufps.schedulequery.entities.VetDailySchedule;
import com.ufps.schedulequery.repositories.VetDailyScheduleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ScheduleProjectionService {

    private final VetDailyScheduleRepository repo;

    public ScheduleProjectionService(VetDailyScheduleRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void handleCreated(AppointmentEventDto dto) {
        upsertFromDto(dto, "BOOKED");
    }

    @Transactional
    public void handleRescheduled(AppointmentEventDto dto, String newDate, String newTime) {
        if (newDate != null) dto.fecha = newDate;
        if (newTime != null) dto.hora = newTime;
        upsertFromDto(dto, "RESCHEDULED");
    }

    @Transactional
    public void handleCancelled(AppointmentEventDto dto) {
        upsertFromDto(dto, "CANCELLED");
    }

    @Transactional
    public void handleAttended(AppointmentEventDto dto) {
        upsertFromDto(dto, "ATTENDED");
    }

    private void upsertFromDto(AppointmentEventDto dto, String status) {
        if (dto.appointmentId == null) return;
        Instant occurred = dto.occurredAt != null ? dto.occurredAt : Instant.now();

        Optional<VetDailySchedule> maybe = repo.findByAppointmentId(dto.appointmentId);
        if (maybe.isPresent()) {
            VetDailySchedule existing = maybe.get();
            Instant existingOccurred = existing.getOccurredAt();
            if (existingOccurred != null && existingOccurred.isAfter(occurred)) {
                // evento más antiguo -> ignorar
                return;
            }
            // actualizar campos
            existing.setVetId(dto.veterinarianId != null ? dto.veterinarianId : existing.getVetId());
            if (dto.fecha != null) existing.setDate(LocalDate.parse(dto.fecha));
            if (dto.hora != null) existing.setSlotTime(LocalTime.parse(dto.hora));
            existing.setPetName(dto.petName != null ? dto.petName : existing.getPetName());
            existing.setClientName(dto.clientName != null ? dto.clientName : existing.getClientName());
            existing.setReason(dto.motivo != null ? dto.motivo : existing.getReason());
            existing.setStatus(status);
            existing.setOccurredAt(occurred);
            repo.save(existing);
        } else {
            VetDailySchedule newRec = getVetDailySchedule(dto, status, occurred);
            repo.save(newRec);
        }
    }

    @NotNull
    private static VetDailySchedule getVetDailySchedule(AppointmentEventDto dto, String status, Instant occurred) {
        VetDailySchedule newRec = new VetDailySchedule();
        newRec.setAppointmentId(dto.appointmentId);
        newRec.setVetId(dto.veterinarianId != null ? dto.veterinarianId : 0L);
        if (dto.fecha != null) newRec.setDate(LocalDate.parse(dto.fecha));
        if (dto.hora != null) newRec.setSlotTime(LocalTime.parse(dto.hora));
        newRec.setPetName(dto.petName);
        newRec.setClientName(dto.clientName);
        newRec.setReason(dto.motivo);
        newRec.setStatus(status);
        newRec.setOccurredAt(occurred);
        return newRec;
    }
}
