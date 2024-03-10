package com.jobserve.reviewms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    @Value("${rabbitmq.review-to-company.queue.name}")
    private String reviewCompanyQueueName;

    @Value("${rabbitmq.review-to-company.routing.key}")
    private String reviewCompanyRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(this.exchangeName);
    }

    @Bean
    public Queue reviewToCompanyQueue() {
        return new Queue(this.reviewCompanyQueueName);
    }

    @Bean
    public Binding reviewToCompanyQueueBinding() {
        return BindingBuilder
                .bind(reviewToCompanyQueue())
                .to(exchange())
                .with(this.reviewCompanyRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
