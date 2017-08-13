package de.maltorpro.shop.service.composite.product;


import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;
import de.maltorpro.shop.util.ServiceUtils;


@Component
public class ProductCompositeIntegration {

	 private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

	    @Inject
	    private ServiceUtils util;
    
	    
	    @Inject
	    @LoadBalanced
	    private RestOperations restTemplate;

	    // -------- //
	    // PRODUCTS //
	    // -------- //

	    @HystrixCommand(fallbackMethod = "defaultProduct")
	    public ResponseEntity<Product> getProduct(int productId) {
	    	
	    	LOG.debug("Will call getProduct with Hystrix protection");
	        
	        String url = "http://product-service/product/" + productId;
	        
	        LOG.debug("GetProduct from URL: {}", url);  
	        
	        ResponseEntity<Product> product = restTemplate.getForEntity(url, Product.class);
	        LOG.debug("GetProduct http-status: {}", product.getStatusCode());
	        LOG.debug("GetProduct body: {}", product.getBody());

	        //Product product = response2Product(resultStr);
	        //Product product = new Product(1, "Mein tolles Produkt", 22);
	        LOG.debug("GetProduct.id: {}", product.getBody().getProductId());

	        return util.createOkResponse(product.getBody());
	    }

	    /**
	     * Fallback method for getProduct()
	     *
	     * @param productId
	     * @return
	     */
	    public ResponseEntity<Product> defaultProduct(int productId) {
	        LOG.warn("Using fallback method for product-service");
	        // If we can't get basic product info we better fail!
	        return util.createResponse(null, HttpStatus.BAD_GATEWAY);
	    }

	    // --------------- //
	    // RECOMMENDATIONS //
	    // --------------- //

	    @HystrixCommand(fallbackMethod = "defaultRecommendations")
	    public ResponseEntity<Recommendation[]> getRecommendations(int productId) {
	        try {
	            LOG.debug("Will call getRecommendations with Hystrix protection");
	            
	            String url = "http://recommendation-service/recommendation/" + productId;
	            
	            LOG.debug("GetRecommendations from URL: {}", url);

	            ResponseEntity<Recommendation[]> recommendations = restTemplate.getForEntity(url, Recommendation[].class);
	            LOG.debug("GetRecommendations http-status: {}", recommendations.getStatusCode());
	            LOG.debug("GetRecommendations body: {}", recommendations.getBody().toString());

	            LOG.debug("GetRecommendations.cnt {}", recommendations.getBody().length);

	            return util.createOkResponse(recommendations.getBody());
	        } catch (Throwable t) {
	            LOG.error("getRecommendations error", t);
	            throw t;
	        }
	    }


	    /**
	     * Fallback method for getRecommendations()
	     *
	     * @param productId
	     * @return
	     */
	    public ResponseEntity<Recommendation[]> defaultRecommendations(int productId) {
	        LOG.warn("Using fallback method for recommendation-service");
	        LOG.debug("GetRecommendations.fallback-cnt {}", 1);
	        
	        Recommendation[] result = {new Recommendation(productId, 1, "Fallback Author 1", 1, "Fallback Content 1")};
	        return util.createResponse(result, HttpStatus.OK);
	    }


	    // ------- //
	    // REVIEWS //
	    // ------- //

	    @HystrixCommand(fallbackMethod = "defaultReviews")
	    public ResponseEntity<Review[]> getReviews(int productId) {
	        LOG.debug("Will call getReviews with Hystrix protection");
	        
	        String url = "http://review-service/review/" + productId;
	       
	        LOG.debug("GetReviews from URL: {}", url);
	        
	        ResponseEntity<Review[]> reviews = restTemplate.getForEntity(url, Review[].class);
	        LOG.debug("GetReviews http-status: {}", reviews.getStatusCode());
	        LOG.debug("GetReviews body: {}", reviews.getBody().toString());

	        LOG.debug("GetReviews.cnt {}", reviews.getBody().length);

	        return util.createOkResponse(reviews.getBody());
	    }


	    /**
	     * Fallback method for getReviews()
	     *
	     * @param productId
	     * @return
	     */
	    public ResponseEntity<Review[]> defaultReviews(int productId) {
	        LOG.warn("Using fallback method for review-service");
	        LOG.debug("GetReviews.fallback-cnt {}", 1);
	        
	        Review[] result = {new Review(productId, 1, "Fallback Author 1", "Fallback Subject 1", "Fallback Content 1")};
	        return util.createResponse(result, HttpStatus.OK);
	    }

}

