//package com.e_commerce.order_api.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.amqp.core.AmqpAdmin;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class DefaultExchangeConfig {
//    private final AmqpAdmin amqpAdmin;
//    public static final String QUEUE = "order_queue";
//
//    @Bean
//    Queue createQueue(){
//        return new Queue(QUEUE, true , false,false);
//    }
//    @Bean
//    public AmqpTemplate orderQueue(ConnectionFactory connectionFactory , MessageConverter messageConverter){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter);
//        rabbitTemplate.setRoutingKey(QUEUE);
//        return rabbitTemplate;
//    }
//    @PostConstruct
//    public void init(){
//        amqpAdmin.declareQueue(createQueue());
//    }
//
//}
package com.e_commerce.order_api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Configuration
public class DefaultExchangeConfig {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExchangeConfig.class);

    public static final String QUEUE = "order_queue";
    private final AmqpAdmin amqpAdmin;

    public DefaultExchangeConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }
    @Bean
    public Queue createQueue() {
        return new Queue(QUEUE, true, false, false);
    }

    @Bean
    public AmqpTemplate orderQueue(final ConnectionFactory connectionFactory,final MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setRoutingKey(QUEUE);
        return rabbitTemplate;
    }
    @PostConstruct
    public void init() {
        try {
            amqpAdmin.declareQueue(createQueue());
            logger.info("Queue {} declared successfully", QUEUE);
        } catch (Exception e) {
            logger.error("Failed to declare queue: {}", QUEUE, e);
        }
    }
}
