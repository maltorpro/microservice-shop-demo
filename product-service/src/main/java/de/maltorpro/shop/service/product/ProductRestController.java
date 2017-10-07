package de.maltorpro.shop.service.product;

import static net.logstash.logback.marker.Markers.append;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.dto.ProductDto;
import de.maltorpro.shop.dto.ProductResourceDto;
import de.maltorpro.shop.model.enums.LogtashMarker;

@RestController
@RefreshScope
public class ProductRestController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

	@Autowired
	ProductService productService;

	@Value("${message:Hello default}")
	private String message;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping("/message")
	String getMessage() {

		LOG.info("/product called, message: {}", message);
		return this.message;
	}

	/**
	 * Retrieve a single product by its id. Sample usage: curl
	 * $HOST:$PORT/product/1
	 *
	 * @param uuid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/{uuid}")
	public ProductResourceDto getProduct(@PathVariable String uuid) {

		LOG.info(append(LogtashMarker.EVENT.name(), "dd"), "Call product service");

		ProductResourceDto productResourceDto = null;
		ProductDto productDto = productService.findByUuid(uuid);
		if (productDto != null) {
			productResourceDto = modelMapper.map(productDto, ProductResourceDto.class);
		}

		if (productResourceDto != null) {
			productResourceDto
					.add(linkTo(methodOn(ProductRestController.class).getProduct("testuuid")).withRel("next"));
		}

		LOG.info(append("dataobject", productResourceDto), "Die daten meines tollen produktes");

		return productResourceDto;
	}

	// @formatter:off
    /**
     * Save a single product.
     * Sample usage: curl -H "Content-Type: application/json" -X POST -d '{"name":"Product Name 1","shortDescription":"Product short description", "longDescription": "Product long description"}' $HOST:$PORT/product
     * @param product
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.POST, value = "/product")
	public ProductDto saveProduct(@RequestBody ProductDto product) {

		return productService.saveProduct(product);
	}
}
