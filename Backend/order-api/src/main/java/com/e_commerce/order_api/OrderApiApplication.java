package com.e_commerce.order_api;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApiApplication.class, args);
	}

}
