package de.maltorpro.shop.service.test.support;


import org.springframework.data.repository.CrudRepository;

import de.maltorpro.shop.model.Product;

public interface ProductTestRepository extends CrudRepository<Product, Long> {

	Product findFirstByProductUuid(String productUuid);

	long deleteByProductUuid(String productUuid);
}
