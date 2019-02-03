package com.reservas.org.rabbitmq;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
public class RabbitService  {
    private final Logger log = LoggerFactory.getLogger(RabbitService.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void RabbitService() throws JsonProcessingException {

        init();
    }

    public void init() throws JsonProcessingException {
    }

    public Boolean send_message (MessageQueue messageQueue) throws JsonProcessingException {

        rabbitTemplate.convertAndSend(RabbitConfiguration.QUEUE_NAME, messageQueue);
        return true;

    }


}

