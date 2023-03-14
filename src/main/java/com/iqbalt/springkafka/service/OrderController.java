package com.iqbalt.springkafka.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iqbalt.springkafka.OrderVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name = "Order Rest Endpoint")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping(path = "/orders", method = RequestMethod.POST)
	@Operation(summary = "Puts an order into a queue", description = "Creates an order and puts it in the source kafka topic")
	public OrderVo createProduct(@Valid @RequestBody OrderVo order){
		return orderService.addOrderToQueue(order);
	}
}
