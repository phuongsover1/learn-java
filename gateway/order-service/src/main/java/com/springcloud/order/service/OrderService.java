package com.springcloud.order.service;

import com.springcloud.order.model.Order;
import com.springcloud.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;

import java.util.Optional;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public List<Order> findAll() {

		return orderRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Order> findById(Long id) {

		return orderRepository.findById(id);
	}

	public Order create(Order order) {
		BigDecimal total = order.getUnitPrice()
				.multiply(BigDecimal.valueOf(order.getQuantity()));
		order.setTotalPrice(total);

		order.setId(null);
		return orderRepository.save(order);
	}

	public boolean delete(Long id) {

		if (!orderRepository.existsById(id)) {
			return false;
		}
		orderRepository.deleteById(id);
		return true;
	}
}