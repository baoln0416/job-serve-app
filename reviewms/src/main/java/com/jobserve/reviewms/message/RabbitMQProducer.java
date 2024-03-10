package com.jobserve.reviewms.message;

import com.jobserve.reviewms.review.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.review-to-company.routing.key}")
    private String reviewCompanyRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(Review review) {
        LOGGER.info(String.format("Sent message -> %s", review.toString()));
        rabbitTemplate.convertAndSend(this.exchangeName, this.reviewCompanyRoutingKey, review);
    }

    public void sendMessage(String message) {
        LOGGER.info(String.format("Sent message -> %s", message));
        rabbitTemplate.convertAndSend(this.exchangeName, this.reviewCompanyRoutingKey, message);
    }
}
