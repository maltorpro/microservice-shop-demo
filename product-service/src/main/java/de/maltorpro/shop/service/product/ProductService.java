package de.maltorpro.shop.service.product;

import de.maltorpro.shop.dto.ProductDto;

public interface ProductService {

	ProductDto saveProduct(ProductDto productDto);

	ProductDto findByUuid(String uuid);
}
