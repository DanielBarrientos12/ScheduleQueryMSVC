package com.ufps.schedulequery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEventDto {
    @JsonProperty("appointmentId")
    public Long appointmentId;

    @JsonProperty("fecha")
    public String fecha; // "YYYY-MM-DD"

    @JsonProperty("hora")
    public String hora; // "HH:MM:SS"

    @JsonProperty("motivo")
    public String motivo;

    @JsonProperty("petName")
    public String petName;

    @JsonProperty("clientName")
    public String clientName;

    @JsonProperty("veterinarianId")
    public Long veterinarianId;

    @JsonProperty("occurredAt")
    public Instant occurredAt;
}

