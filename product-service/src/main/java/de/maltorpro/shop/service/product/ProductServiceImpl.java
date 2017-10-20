package de.maltorpro.shop.service.product;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.maltorpro.shop.model.Product;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductPagingRepository productPagingRepository;

	@Override
	public Product saveProduct(Product product) {

		if (product == null) {
			throw new IllegalArgumentException("product cannot be null");
		}
		Product productDB = null;

		if (!StringUtils.isEmpty(product.getProductUuid())) {
			productDB = productRepository.findFirstByProductUuid(product.getProductUuid());
		}

		// set uuid for the new product
		if (productDB == null) {

			productDB = product;
			productDB.setProductUuid(UUID.randomUUID().toString());
		}

		return productRepository.save(productDB);
	}

	@Override
	public Product findByUuid(String uuid) {

		return productRepository.findFirstByProductUuid(uuid);
	}

}
