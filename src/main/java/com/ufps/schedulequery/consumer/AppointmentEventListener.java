package com.ufps.schedulequery.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.schedulequery.dto.AppointmentEventDto;
import com.ufps.schedulequery.service.ScheduleProjectionService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {

    private final ObjectMapper mapper;
    private final ScheduleProjectionService projectionService;

    public AppointmentEventListener(ObjectMapper mapper, ScheduleProjectionService projectionService) {
        this.mapper = mapper;
        this.projectionService = projectionService;
    }

    @RabbitListener(queues = "${app.queue}")
    public void receive(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String body = new String(message.getBody());
        try {
            AppointmentEventDto dto = mapper.readValue(body, AppointmentEventDto.class);

            switch (routingKey) {
                case "appointment.created" -> projectionService.handleCreated(dto);
                case "appointment.rescheduled" -> projectionService.handleRescheduled(dto, null, null);
                case "appointment.cancelled" -> projectionService.handleCancelled(dto);
                case "appointment.attended" -> projectionService.handleAttended(dto);
                default -> {
                }
            }
            System.out.println("✅ Evento procesado: " + routingKey + " appointmentId=" + dto.getAppointmentId());
        } catch (Exception e) {
            System.err.println("❌ Error procesando evento (routingKey=" + routingKey + "): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
