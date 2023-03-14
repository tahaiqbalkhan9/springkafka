package com.iqbalt.springkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqbalt.springkafka.OrderVo;

@Service
public class OrderServiceImpl implements OrderService {

	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    
	@Value("${topic.target}")
    private String targetTopic;
	
	@Value("${topic.source}")
	private String sourceTopic;
	
    private final ObjectMapper objectMapper = new ObjectMapper();
    
	@Autowired
	private KafkaTemplate<Long, OrderVo> kafkaOrderTemplate;

	
	@Override
	@KafkaListener(id = "orderId", topics = {"topic-source"}, containerFactory = "singleFactory")
	public void consume(OrderVo order) {
		
		LOGGER.info("-> consumed {}", writeValueAsString(order));
		
		sendUpdateToCustomer(order);

	}

    private String writeValueAsString(OrderVo orderVo) {
        try {
            return objectMapper.writeValueAsString(orderVo);
        } catch (JsonProcessingException e) {
        	LOGGER.error("Error happens during json processing", e);
            throw new RuntimeException("Writing value to JSON failed: " + orderVo.toString());
        }
    }

	@Override
	public void sendUpdateToCustomer(OrderVo order) {
		LOGGER.info("-> Now sending order to Target Topic {}", order.toString());
		kafkaOrderTemplate.send(targetTopic, order);
		
	}

	@Override
	public OrderVo addOrderToQueue(OrderVo order) {
		LOGGER.info("-> Now Adding order to Source Topic {}", order.toString());
		kafkaOrderTemplate.send(sourceTopic, order);
		return order;
	}
}
