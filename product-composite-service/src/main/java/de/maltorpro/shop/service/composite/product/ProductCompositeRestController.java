package de.maltorpro.shop.service.composite.product;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;
import de.maltorpro.shop.service.composite.product.model.ProductAggregated;
import de.maltorpro.shop.utils.ServiceUtils;

@RestController
public class ProductCompositeRestController {

    private static final Logger LOG = LoggerFactory
            .getLogger(ProductCompositeRestController.class);

    @Autowired
    ProductCompositeService productCompositeService;

    @Autowired
    ServiceUtils util;

    @RequestMapping("/")
    public String getProduct() {
        return "{\"timestamp\":\"" + new Date()
                + "\",\"content\":\"Hello from ProductAPi\"}";
    }

    @PreAuthorize("hasAuthority('ROLE_DEVELOPERS')")
    @RequestMapping("/{uuid}")
    public ResponseEntity<ProductAggregated> getProduct(
            @PathVariable String uuid, Principal currentUser) {

        LOG.info("/product called");

        // 1. First get mandatory product information
        ResponseEntity<Product> productResult = productCompositeService
                .getProduct(uuid);

        if (!productResult.getStatusCode().is2xxSuccessful()) {
            // We can't proceed, return whatever fault we got from the
            // getProduct call
            return util.createResponse(null, productResult.getStatusCode());
        }

        // 2. Get optional recommendations
        List<Recommendation> recommendations = null;
        ResponseEntity<Recommendation[]> recommendationResult = productCompositeService
                .getRecommendations(uuid);
        if (!recommendationResult.getStatusCode().is2xxSuccessful()) {
            // Something went wrong with getRecommendations, simply skip the
            // recommendation-information in the response
            LOG.debug("Call to getRecommendations failed: {}",
                    recommendationResult.getStatusCode());
        } else {
            Recommendation[] result = recommendationResult.getBody();
            if (result != null && result.length > 0) {
                recommendations = Arrays.asList(result);
            }
        }

        // 3. Get optional reviews
        List<Review> reviews = null;
        ResponseEntity<Review[]> reviewsResult = productCompositeService
                .getReviews(uuid);

        if (!reviewsResult.getStatusCode().is2xxSuccessful()) {
            // Something went wrong with getReviews, simply skip the
            // review-information in the response
            LOG.debug("Call to getReviews failed: {}",
                    reviewsResult.getStatusCode());
        } else {
            Review[] result = reviewsResult.getBody();
            if (result != null && result.length > 0) {
                reviews = Arrays.asList(result);
            }
        }

        return util.createOkResponse(new ProductAggregated(
                productResult.getBody(), recommendations, reviews));
    }
}
