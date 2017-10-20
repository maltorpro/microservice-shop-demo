package de.maltorpro.shop.service.product;

import static net.logstash.logback.marker.Markers.append;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.enums.LogtashMarker;

@RestController
@RefreshScope
public class ProductRestController {

	private static final Logger log = LoggerFactory.getLogger(ProductRestController.class);

	@Autowired
	ProductService productService;

	@Value("${message:Hello default}")
	private String message;

	@RequestMapping("/message")
	String getMessage() {

		log.info("/product called, message: {}", message);
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
	public Product getProduct(@PathVariable String uuid) {

		log.info(append(LogtashMarker.EVENT.name(), "dd"), "Call product service");

		Product product = productService.findByUuid(uuid);

		if (product != null) {
			product.add(linkTo(methodOn(ProductRestController.class).getProduct("testuuid")).withRel("next"));
		}

		log.info(append("dataobject", product), "Die daten meines tollen produktes");

		return product;
	}

	// @formatter:off
    /**
     * Save a single product.
     * Sample usage: curl -H "Content-Type: application/json" -X POST -d '{"name":"Product Name 1","shortDescription":"Product short description", "longDescription": "Product long description"}' $HOST:$PORT/product
     * @param product
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.POST, value = "/product")
	public Product saveProduct(@RequestBody Product product) {

		return productService.saveProduct(product);
	}
}
