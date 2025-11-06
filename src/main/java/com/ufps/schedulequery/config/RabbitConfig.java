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

    @Value("${app.routing-keys[0]}")
    private String rk0;
    @Value("${app.routing-keys[1]}")
    private String rk1;
    @Value("${app.routing-keys[2]}")
    private String rk2;
    @Value("${app.routing-keys[3]}")
    private String rk3;

    @Bean
    public TopicExchange appExchange() {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    /**
     * Cola principal. Declaramos argumentos para que en caso de reject/ttl expire vaya a la DLQ.
     * NOTA: usamos dead-letter-exchange "" (exchange por defecto) y routing key = dlqName.
     */
    @Bean
    public Queue scheduleQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")            // exchange por defecto
                .withArgument("x-dead-letter-routing-key", dlqName)   // mensaje irá a la DLQ
                .build();
    }

    @Bean
    public Queue scheduleDlq() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding bindCreated(Queue scheduleQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(scheduleQueue).to(appExchange).with(rk0);
    }

    @Bean
    public Binding bindRescheduled(Queue scheduleQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(scheduleQueue).to(appExchange).with(rk1);
    }

    @Bean
    public Binding bindCancelled(Queue scheduleQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(scheduleQueue).to(appExchange).with(rk2);
    }

    @Bean
    public Binding bindAttended(Queue scheduleQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(scheduleQueue).to(appExchange).with(rk3);
    }

}
