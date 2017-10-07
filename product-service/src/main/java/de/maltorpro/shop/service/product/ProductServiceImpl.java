package de.maltorpro.shop.service.product;

import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.maltorpro.shop.dto.ProductDto;
import de.maltorpro.shop.model.Product;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductPagingRepository productPagingRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ProductDto saveProduct(ProductDto productDto) {

		if (productDto == null) {
			throw new IllegalArgumentException("productDto cannot be null");
		}
		Product product = null;

		if (!StringUtils.isEmpty(productDto.getProductUuid())) {
			product = productRepository.findFirstByProductUuid(productDto.getProductUuid());
		}

		// set uuid for the new product
		if (product == null) {
			product = modelMapper.map(productDto, Product.class);
			product.setProductUuid(UUID.randomUUID().toString());
		}

		product = productRepository.save(product);

		ProductDto poductResult = modelMapper.map(product, ProductDto.class);
		return poductResult;
	}

	@Override
	public ProductDto findByUuid(String uuid) {

		Product product = productRepository.findFirstByProductUuid(uuid);
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		return productDto;
	}

}
