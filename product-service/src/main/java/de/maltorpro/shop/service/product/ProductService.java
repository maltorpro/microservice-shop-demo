package de.maltorpro.shop.service.product;

import org.springframework.data.domain.Page;

import de.maltorpro.shop.model.Product;

public interface ProductService {

	Product saveProduct(Product product);

	Product findByUuid(String uuid);

	long deleteByUuid(String uuid);

	Page<Product> listAllProductsByPage(int page, int size);
}
