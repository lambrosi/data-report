package com.lucasambrosi.fileprocessor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

    @Value("${application.messaging.queue}")
    private String fileReceivedQueue;
    @Value("${application.messaging.exchange}")
    private String fileReceivedExchange;

    @Bean
    public Queue fileReceivedQueue() {
        return QueueBuilder.durable(fileReceivedQueue)
                .build();
    }

    @Bean
    public TopicExchange fileReceivedExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(fileReceivedExchange)
                .durable(true)
                .build();
    }

    @Bean
    public Binding bindFileReceivedExchangeToQueue() {
        return BindingBuilder.bind(fileReceivedQueue()).to(fileReceivedExchange()).with("#");
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();

        ObjectMapper objectMapper = mappingJackson2MessageConverter.getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return mappingJackson2MessageConverter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
