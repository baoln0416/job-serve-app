package com.jobserve.companyms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.company-to-job.queue.name}")
    private String companyToJobMessageQueue;

    @Value("rabbitmq.company-to-job.routing.key")
    private String companyToJobRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(this.exchangeName);
    }

    @Bean
    public Queue companyJobQueue() {
        return new Queue(this.companyToJobMessageQueue);
    }

    @Bean
    public Binding companyJobQueueBiding() {
        return BindingBuilder
                .bind(companyJobQueue())
                .to(exchange())
                .with(this.companyToJobRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
