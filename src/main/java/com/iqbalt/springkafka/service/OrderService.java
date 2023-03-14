package com.iqbalt.springkafka.service;

import com.iqbalt.springkafka.OrderVo;

public interface OrderService {

	void consume(OrderVo order);
	
	OrderVo addOrderToQueue(OrderVo order);
	
	void sendUpdateToCustomer(OrderVo order);
	
}
