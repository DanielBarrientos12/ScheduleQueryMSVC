package com.ufps.schedulequery.repositories;

import com.ufps.schedulequery.entities.VetDailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VetDailyScheduleRepository extends JpaRepository <VetDailySchedule, Long> {
    Optional<VetDailySchedule> findByAppointmentId(Long appointmentId);
    List<VetDailySchedule> findByVetIdAndDateOrderBySlotTime(Long vetId, LocalDate date);
    List<VetDailySchedule> findByVetId(Long vetId);
}
