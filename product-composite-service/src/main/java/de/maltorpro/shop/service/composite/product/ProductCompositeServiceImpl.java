package de.maltorpro.shop.service.composite.product;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;
import de.maltorpro.shop.util.ServiceUtils;

@Service("productCompositeService")
@Transactional
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private static final Logger log = LoggerFactory
            .getLogger(ProductCompositeServiceImpl.class);

    @Autowired
    private ServiceUtils util;

    @Autowired
    private RestTemplate restTemplate;

    // -------- //
    // PRODUCTS //
    // -------- //

    @HystrixCommand(fallbackMethod = "defaultProduct")
    public ResponseEntity<Product> getProduct(String productUuid) {

        log.debug("Will call getProduct with Hystrix protection");

        String url = "http://product-service/product/{uuid}";

        log.debug("GetProduct from URL: {}", url);

        ResponseEntity<Product> product = restTemplate.getForEntity(url,
                Product.class, productUuid);
        log.debug("GetProduct http-status: {}", product.getStatusCode());
        log.debug("GetProduct body: {}", product.getBody());

        return util.createOkResponse(product.getBody());
    }

    /**
     * Fallback method for getProduct()
     *
     * @param productId
     * @return
     */
    public ResponseEntity<Product> defaultProduct(String productUuid) {
        log.warn("Using fallback method for product-service");
        // If we can't get basic product info we better fail!
        return util.createResponse(null, HttpStatus.BAD_GATEWAY);
    }

    // --------------- //
    // RECOMMENDATIONS //
    // --------------- //

    @HystrixCommand(fallbackMethod = "defaultRecommendations")
    public ResponseEntity<Recommendation[]> getRecommendations(
            String productUuid) {
        try {
            log.debug("Will call getRecommendations with Hystrix protection");

            String url = "http://recommendation-service/recommendation/product/{uuid}";

            log.debug("GetRecommendations from URL: {}", url);

            ResponseEntity<Recommendation[]> recommendations = restTemplate
                    .getForEntity(url, Recommendation[].class, productUuid);
            log.debug("GetRecommendations http-status: {}",
                    recommendations.getStatusCode());

            if (log.isDebugEnabled()) {
                log.debug("GetRecommendations body: {}",
                        Arrays.toString(recommendations.getBody()));
            }

            log.debug("GetRecommendations.cnt {}",
                    recommendations.getBody().length);

            return util.createOkResponse(recommendations.getBody());
        } catch (Exception t) {
            log.error("getRecommendations error", t);
            throw t;
        }
    }

    /**
     * Fallback method for getRecommendations()
     *
     * @param productId
     * @return
     */
    public ResponseEntity<Recommendation[]> defaultRecommendations(
            String productUuid) {
        log.warn("Using fallback method for recommendation-service");
        log.debug("GetRecommendations.fallback-cnt {}", 1);

        Recommendation[] result = { new Recommendation() };
        return util.createResponse(result, HttpStatus.OK);
    }

    // ------- //
    // REVIEWS //
    // ------- //

    @HystrixCommand(fallbackMethod = "defaultReviews")
    public ResponseEntity<Review[]> getReviews(String productUuid) {
        log.debug("Will call getReviews with Hystrix protection");

        String url = "http://review-service/reviews/product/{uuid}/{page}/{size}";

        log.debug("GetReviews from URL: {}", url);

        ResponseEntity<Review[]> reviews = restTemplate.getForEntity(url,
                Review[].class, productUuid, 0, 10);
        log.debug("GetReviews http-status: {}", reviews.getStatusCode());

        if (log.isDebugEnabled()) {
            log.debug("GetReviews body: {}",
                    Arrays.toString(reviews.getBody()));
        }

        log.debug("GetReviews.cnt {}", reviews.getBody().length);

        return util.createOkResponse(reviews.getBody());
    }

    /**
     * Fallback method for getReviews()
     *
     * @param productId
     * @return
     */
    public ResponseEntity<Review[]> defaultReviews(String productUuid) {
        log.warn("Using fallback method for review-service");
        log.debug("GetReviews.fallback-cnt {}", 1);

        Review[] result = { new Review() };
        return util.createResponse(result, HttpStatus.OK);
    }

}
