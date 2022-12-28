package com.bats.lite.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {

  @Bean
  public Queue createLine() {
    return QueueBuilder.nonDurable("usuario.cadastrado").build();
  }

  @Bean
  public RabbitAdmin createAdmin(ConnectionFactory conn) {
    return new RabbitAdmin(conn);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationListener(
    RabbitAdmin admin
  ) {
    return event -> admin.initialize();
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    return new Jackson2JsonMessageConverter(mapper);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
    ConnectionFactory factory,
    Jackson2JsonMessageConverter messageConverter
  ) {
    RabbitTemplate template = new RabbitTemplate(factory);
    template.setMessageConverter(messageConverter);
    return template;
  }
}
