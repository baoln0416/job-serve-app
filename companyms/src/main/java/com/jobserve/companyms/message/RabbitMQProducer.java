package com.jobserve.companyms.message;

import com.jobserve.companyms.company.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("rabbitmq.company-to-job.routing.key")
    private String companyToJobRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        LOGGER.info(String.format("Sent message -> %s", message));
        rabbitTemplate.convertAndSend(this.exchangeName, this.companyToJobRoutingKey, message);
    }

    public void sendJsonMessage(Company company) {
        LOGGER.info(String.format("Sent json message -> %s", company.toString()));
        rabbitTemplate.convertAndSend(this.exchangeName, this.companyToJobRoutingKey, company);
    }

}
