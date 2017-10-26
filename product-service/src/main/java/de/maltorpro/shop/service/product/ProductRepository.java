package de.maltorpro.shop.service.product;

import org.springframework.data.repository.CrudRepository;

import de.maltorpro.shop.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	Product findFirstByProductUuid(String productUuid);

	long deleteByProductUuid(String productUuid);
}
