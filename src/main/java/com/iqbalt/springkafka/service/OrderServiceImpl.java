package com.iqbalt.springkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.iqbalt.springkafka.OrderVo;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	@KafkaListener(id = "orderId", topics = {"topic-source"}, containerFactory = "singleFactory")
	public void consume(OrderVo order) {
		
		LOGGER.info("-> consumed {}", writeValueAsString(order));

	}

    private String writeValueAsString(OrderVo orderVo) {
        try {
            return objectMapper.writeValueAsString(orderVo);
        } catch (JsonProcessingException e) {
        	LOGGER.error("Error happens during json processing", e);
            throw new RuntimeException("Writing value to JSON failed: " + orderVo.toString());
        }
    }
}
