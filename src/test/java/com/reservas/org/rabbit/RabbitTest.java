package com.reservas.org.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reservas.org.ReservasApp;
import com.reservas.org.config.ApplicationProperties;
import com.reservas.org.rabbitmq.MessageQueue;
import com.reservas.org.rabbitmq.RabbitService;
import com.reservas.org.rabbitmq.TypeMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservasApp.class)
@Import(RabbitService.class)
@EnableConfigurationProperties({ApplicationProperties.class})
public class RabbitTest {

    @Autowired
    RabbitService rabbitService;

    @Test
    public void  TestWhenThereAreConnection () throws InterruptedException, JsonProcessingException {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.message_body="HI";
        messageQueue.message_type= TypeMessage.REGISTER;

        rabbitService.send_message(messageQueue);

    }
}
