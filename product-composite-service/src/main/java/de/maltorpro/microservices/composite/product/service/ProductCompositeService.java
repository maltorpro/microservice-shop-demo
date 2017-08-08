package de.maltorpro.microservices.composite.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.microservices.composite.product.model.ProductAggregated;
import de.maltorpro.microservices.model.Product;
import de.maltorpro.microservices.model.Recommendation;
import de.maltorpro.microservices.model.Review;
import de.maltorpro.microservices.util.ServiceUtils;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
public class ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);

    @Autowired
    ProductCompositeIntegration integration;

    @Autowired
    ServiceUtils util;

    @RequestMapping("/")
    public String getProduct() {
        return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from ProductAPi\"}";
    }
    

    @PreAuthorize("hasAuthority('ROLE_DEVELOPERS')")
    @RequestMapping("/{productId}")
    public ResponseEntity<ProductAggregated> getProduct(@PathVariable int productId,Principal currentUser) {
    	
    	LOG.info("/product called");
    	
        // 1. First get mandatory product information
        ResponseEntity<Product> productResult = integration.getProduct(productId);

        if (!productResult.getStatusCode().is2xxSuccessful()) {
            // We can't proceed, return whatever fault we got from the getProduct call
            return util.createResponse(null, productResult.getStatusCode());
        }
        
        
        // 2. Get optional recommendations
        
        List<Recommendation> recommendations = null;
        ResponseEntity<Recommendation[]> recommendationResult = integration.getRecommendations(productId);
        if (!recommendationResult.getStatusCode().is2xxSuccessful()) {
            // Something went wrong with getRecommendations, simply skip the recommendation-information in the response
            LOG.debug("Call to getRecommendations failed: {}", recommendationResult.getStatusCode());
        } else {
            Recommendation[] result = recommendationResult.getBody();
            if(result != null && result.length > 0) {
            	recommendations = Arrays.asList(result);
            }
        }
                
        
        // 3. Get optional reviews
        
        List<Review> reviews = null;
        ResponseEntity<Review[]> reviewsResult = integration.getReviews(productId);
        
        if (!reviewsResult.getStatusCode().is2xxSuccessful()) {
            // Something went wrong with getReviews, simply skip the review-information in the response
            LOG.debug("Call to getReviews failed: {}", reviewsResult.getStatusCode());
        } else {
        	Review[] result = reviewsResult.getBody();
        	if(result != null && result.length > 0) {
        		reviews = Arrays.asList(result);
            }
        }
        

        return util.createOkResponse(new ProductAggregated(productResult.getBody(), recommendations, reviews));
    }
}
