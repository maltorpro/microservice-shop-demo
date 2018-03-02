package de.maltorpro.shop.service.product;

import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.maltorpro.shop.model.Product;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductPagingRepository productPagingRepository;

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
		} else { // update product

			productDB.setUpdateDate(new Date());
			productDB.setName(product.getName());
			productDB.setShortDescription(product.getShortDescription());
			productDB.setLongDescription(product.getLongDescription());
		}

		return productRepository.save(productDB);
	}

	@Override
	public Product findByUuid(String uuid) {

		return productRepository.findFirstByProductUuid(uuid);
	}

	@Override
	public long deleteByUuid(String uuid) {

		return productRepository.deleteByProductUuid(uuid);
	}

	@Override
	public Page<Product> listAllProductsByPage(int page, int size) {

		return productPagingRepository.findAll(PageRequest.of(page, size));
	}

}
