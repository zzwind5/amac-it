package com.aerohive.a3.amacit.conf;

import java.util.Collections;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    private static final String MESSAGE_TTL = "x-message-ttl";

    @Bean
    public Queue amacInboundQueue(@Value("${amac.messaging.queue.request}") final String name,
                                  @Value("${amac.messaging.queue.request.ttl}") final Long ttl) {
        return new Queue(name, true, false, false, Collections.singletonMap(MESSAGE_TTL, ttl));
    }

    @Bean
    public Queue amacOutBoundInventoryQueue(@Value("${amac.messaging.queue.event.inventory}") final String name,
                                            @Value("${amac.messaging.queue.event.inventory.ttl}") final Long ttl) {
        return new Queue(name, true, false, false, Collections.singletonMap(MESSAGE_TTL, ttl));
    }

    @Bean
    public Queue amacOutBoundReportQueue(@Value("${amac.messaging.queue.event.report}") final String name,
                                         @Value("${amac.messaging.queue.event.report.ttl}") final Long ttl) {
        return new Queue(name, true, false, false, Collections.singletonMap(MESSAGE_TTL, ttl));
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(final ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                                         final Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(final ConnectionFactory connectionFactory,
                                                                               final Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

}
