package com.asish.reactive.kafka.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ReactiveKafkaConsumer {

  private ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

  public ReactiveKafkaConsumer(
      ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate) {
    this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
  }

  @EventListener(ApplicationStartedEvent.class)
  private Mono<Void> startKafkaConsumer() {
    return reactiveKafkaConsumerTemplate
        .receiveAutoAck()
        .doOnNext(consumerRecord -> proecessRecord(consumerRecord.value()))
        //.map(ConsumerRecord<String, String>::value);
        .then();
  }

  private void proecessRecord(String value) {
    log.info("received message is : {}", value);
  }

}
