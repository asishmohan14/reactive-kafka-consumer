package com.asish.reactive.kafka.consumer.config;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

@Slf4j
@Configuration
public class ReactiveConsumerConfig {

  @Autowired
  private KafkaProperties kafkaProperties;

  @Value("${app.reactive.kafka.topic.name}")
  private String topicName;

  @Bean
  public ReceiverOptions<String, String> kafkaReceiverOptions(KafkaProperties kafkaProperties) {
    return ReceiverOptions.<String, String>create(kafkaProperties.buildConsumerProperties())
        .consumerProperty(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, "false")
        .consumerProperty(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false")
        .consumerProperty(JsonDeserializer.VALUE_DEFAULT_TYPE, String.class)
        .subscription(Collections.singletonList(topicName));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
      ReceiverOptions<String, String> kafkaReceiverOptions) {
    log.info("created consumer template");
    return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
  }

}
