package com.ufps.schedulequery.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.exchange}")
    private String exchangeName;

    @Value("${app.queue}")
    private String queueName;

    @Value("${app.dlq}")
    private String dlqName;

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue scheduleQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder.bind(scheduleQueue()).to(eventsExchange()).with("appointment.created");
    }

    @Bean
    public Binding rescheduledBinding() {
        return BindingBuilder.bind(scheduleQueue()).to(eventsExchange()).with("appointment.rescheduled");
    }

    @Bean
    public Binding cancelledBinding() {
        return BindingBuilder.bind(scheduleQueue()).to(eventsExchange()).with("appointment.cancelled");
    }

    @Bean
    public Binding attendedBinding() {
        return BindingBuilder.bind(scheduleQueue()).to(eventsExchange()).with("appointment.attended");
    }
}
