package de.maltorpro.shop.service.product;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Product;

@RestController
@RefreshScope
public class ProductRestController {

	@Autowired
	ProductService productService;

	// @formatter:off
	/**
	 * Retrieve a single product by its uuid.
	 * Sample usage: curl $HOST:$PORT/product/550e8400-e29b-11d4-a716-446655440000
	 *
	 * @param uuid
	 * @return
	 */
	 // @formatter:on
	@RequestMapping(method = RequestMethod.GET, value = "/product/{uuid}")
	public Product getProduct(@PathVariable String uuid) {

		Product product = productService.findByUuid(uuid);
		if (product != null) {
			product.add(linkTo(methodOn(ProductRestController.class).getProduct("testuuid")).withRel("next"));
		}

		return product;
	}

	// @formatter:off
    /**
     * Save or update a single product.
     * Sample usage: curl $HOST:$PORT/product -H "Content-Type: application/json" -X POST -d '{"name":"Product Name 1","shortDescription":"Product short description", "longDescription": "Product long description"}' 
     * @param product
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.POST, value = "/product")
	public Product saveProduct(@RequestBody Product product) {

		return productService.saveProduct(product);
	}

	// @formatter:off
    /**
     * Delete a single product by its uuid.
     * Sample usage: curl $HOST:$PORT/product/550e8400-e29b-11d4-a716-446655440000
     *
     * @param uuid
     * @return Number of deleted products.
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.DELETE, value = "/product/{uuid}")
	public long deleteProduct(@PathVariable String uuid) {

		return productService.deleteByUuid(uuid);
	}

	// @formatter:off
    /**
     * Get all products via paging.
     * Sample usage: curl $HOST:$PORT/products/
     *
     * @param pageable paging objects
     * @return List of products.
     */
    // @formatter:on
	@RequestMapping(value = "/products/{page}/{size}", method = RequestMethod.GET)
	Page<Product> getProducts(@PathVariable int page, @PathVariable int size) {

		return productService.listAllProductsByPage(page, size);
	}
}
