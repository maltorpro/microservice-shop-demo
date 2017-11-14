package de.maltorpro.shop.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Review;

@RestController
public class ReviewRestController {

	@Autowired
	private ReviewService reviewService;

	// @formatter:off
    /**
     * Get all reviews of a product via paging.
     * Sample usage: curl $HOST:$PORT/reviews/product/550e8400-e29b-11d4-a716-446655440000/0/1
     *
     * @param pageable paging objects
     * @return List of reviews.
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.GET, value = "/reviews/product/{uuid}/{page}/{size}")
	public Page<Review> getReviews(@PathVariable String uuid, @PathVariable int page, @PathVariable int size) {

		return reviewService.findByProductUuid(uuid, page, size);
	}

	// @formatter:off
    /**
     * Save or update a  review of a Product.
     * Sample usage: $ curl $HOST:$PORT/product -i -X POST -H 'Content-Type: application/json' -d 
     * '{
	 *	  "creationDate" : "2017-11-09 21:58 PM UTC",
	 *	  "updateDate" : "2017-11-09 21:58 PM UTC",
	 *	  "reviewId" : null,
	 *	  "reviewUuid" : null,
	 *	  "author" : "Max Mustermann2",
	 *	  "rating" : 3,
	 *	  "reviewText" : "Nice product2",
	 *	  "product" : {
	 *	    "creationDate" : "2017-11-09 21:58 PM UTC",
	 *	    "updateDate" : "2017-11-09 21:58 PM UTC",
	 *	    "productId" : 1001,
	 *	    "productUuid" : "41a2f12d-0271-4638-b4ba-78557a2d15eb",
	 *	    "name" : "Product2",
	 *	    "shortDescription" : "product2 short description",
	 *	    "longDescription" : "product2 long description"
	 *	  }
	 *	}'
     * @param review
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.POST, value = "/review")
	public Review saveReview(@RequestBody Review review) {

		return reviewService.saveReview(review);
	}

	// @formatter:off
    
	/**
     * Deletes a product review by its uuid.
     * Sample usage: curl $HOST:$PORT/review/5899329c-3b14-4548-b502-79fbf96ab362 -i -X DELETE -H 'Accept: application/json'
     * @param uuid
     * @return Number of deleted reviews.
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.DELETE, value = "/review/{uuid}")
	public long deleteReview(@PathVariable String uuid) {

		return reviewService.deleteByUuid(uuid);
	}
}
