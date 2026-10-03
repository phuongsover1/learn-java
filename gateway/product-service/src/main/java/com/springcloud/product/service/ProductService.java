package com.springcloud.product.service;

import com.springcloud.product.model.Product;
import com.springcloud.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public List<Product> findAll() {

		return productRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Product> findById(Long id) {

		return productRepository.findById(id);
	}

	public Product create(Product product) {

		product.setId(null);
		return productRepository.save(product);
	}

	public Optional<Product> update(Long id, Product product) {

		return productRepository.findById(id).map(existing -> {
			existing.setName(product.getName());
			existing.setPrice(product.getPrice());

			return productRepository.save(existing);
		});
	}

	public boolean delete(Long id) {

		if (!productRepository.existsById(id)) {
			return false;
		}
		productRepository.deleteById(id);
		return true;
	}
}