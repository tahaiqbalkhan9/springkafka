package com.iqbalt.springkafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfiguration {

      @Value("${kafka.server}")
      private String kafkaServer;

      @Value("${kafka.producer.id}")
      private String kafkaProducerId;

      @Bean
      Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
            return props;
      }

      @Bean
      ProducerFactory<Long, OrderVo> producerOrderFactory() {
            return new DefaultKafkaProducerFactory<>(producerConfigs());
      }

      @Bean
      KafkaTemplate<Long, OrderVo> kafkaTemplate() {
            KafkaTemplate<Long, OrderVo> template = new KafkaTemplate<>(producerOrderFactory());
            template.setMessageConverter(new StringJsonMessageConverter());
            return template;
      }
}