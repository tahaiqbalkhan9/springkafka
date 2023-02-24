package com.iqbalt.springkafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaConsumerConfiguration {

      @Value("${kafka.server}")
      private String kafkaServer;

      @Value("${kafka.group.id}")
      private String kafkaGroupId;

      @Bean
      KafkaListenerContainerFactory<?> batchFactory() {
            ConcurrentKafkaListenerContainerFactory<Long, OrderVo> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            factory.setBatchListener(true);
            factory.setMessageConverter(new BatchMessagingMessageConverter(converter()));
            return factory;
      }

      @Bean
      KafkaListenerContainerFactory<?> singleFactory() {
            ConcurrentKafkaListenerContainerFactory<Long, OrderVo> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            factory.setBatchListener(false);
            factory.setMessageConverter(new StringJsonMessageConverter());
            return factory;
      }

      @Bean
      ConsumerFactory<Long, OrderVo> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
      }

      @Bean
      KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
            return new ConcurrentKafkaListenerContainerFactory<>();
      }

      @Bean
      Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            //props.put(JsonDeserializer.TYPE_MAPPINGS, "Order:com.iqbalt.springkafka.OrderVo");
            

            
            return props;
      }

      @Bean
      StringJsonMessageConverter converter() {
            return new StringJsonMessageConverter();
      }
}