package de.maltorpro.shop.service.product;

import de.maltorpro.shop.model.Product;

public interface ProductService {

	Product saveProduct(Product productDto);

	Product findByUuid(String uuid);
}
