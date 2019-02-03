package com.reservas.org.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reservas.org.config.ApplicationProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
public class RabbitConfiguration {

    public static final String QUEUE_NAME = "register-queue";
    private static final String EXCHANGE_NAME = "register-queue-exchange";


    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.user}")
    private String user;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.virtualhost}")
    private String virtualhost;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualhost);

        return connectionFactory;

    }


    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());

        return template;
    }


    @Bean
    public Queue queue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        BindingBuilder.bind(queue).to(exchange()).with(QUEUE_NAME);
        return queue;
    }


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .modules(new JavaTimeModule())
            .dateFormat(new StdDateFormat())
            .timeZone("Asia/Tokyo")
            .build();
        Jackson2JsonMessageConverter jackson2JsonMessageConverter
            = new Jackson2JsonMessageConverter(objectMapper);
        return jackson2JsonMessageConverter;

    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;

    }
}
