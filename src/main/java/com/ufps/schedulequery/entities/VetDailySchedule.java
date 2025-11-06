package com.ufps.schedulequery.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vet_daily_schedule", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"appointment_id"})
})
@Data
public class VetDailySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "vet_id", nullable = false)
    private Long vetId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "slot_time", nullable = false)
    private LocalTime slotTime;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "occurred_at")
    private Instant occurredAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}
