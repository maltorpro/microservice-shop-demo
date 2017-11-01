package de.maltorpro.shop.service.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Recommendation;

@RestController
@RefreshScope
public class RecommendationRestController {

	@Autowired
	private RecommendationService recommendationService;

	// @formatter:off
	/**
	 * Returns all recommendations for a selected product.
	 * Sample usage: curl $HOST:$PORT/recommendation/product/550e8400-e29b-11d4-a716-446655440000
	 *
	 * @param uuid
	 * @return
	 */
	 // @formatter:on
	@RequestMapping(method = RequestMethod.GET, value = "/recommendation/product/{uuid}")
	public Recommendation getProductRecommendation(@PathVariable String uuid) {

		return recommendationService.findByProductUuid(uuid);

	}

	// @formatter:off
    /**
     * Delete a recommendation for for a selected product..
     * Sample usage: $ curl 'http://localhost:8080/recommendation/product/c7fec008-6e3b-47f9-8591-65c759fce363' -i -X DELETE -H 'Accept: application/json'
     *
     * @param uuid
     * @return Number of deleted recommendations.
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.DELETE, value = "/recommendation/product/{uuid}")
	public long deleteProductRecommendation(@PathVariable String uuid) {

		return recommendationService.deleteByProductUuid(uuid);
	}

	// @formatter:off
    /**
     * Save or update a recommendations.
     * Sample usage: $ curl 'http://localhost:8080/recommendation' -i -X POST -H 'Content-Type: application/json' -d 
     * '{
     * "creationDate" : "2017-11-01 15:24 PM UTC",
     * "updateDate" : "2017-11-01 15:24 PM UTC",
     * "recommendationUuid" : "41eecce8-d99d-401c-b113-930ef5e9d7d9",
     * "recommendationFor" : {
     * "creationDate" : "2017-11-01 15:24 PM UTC",
     * "updateDate" : "2017-11-01 15:24 PM UTC",
     * "productUuid" : "c7fec008-6e3b-47f9-8591-65c759fce363",
     * "name" : "Product5",
     * "shortDescription" : "product5 short description",
     * "longDescription" : "product5 long description"
     *},
	 * "recommendations" : [ {
	 *   "creationDate" : "2017-11-01 15:24 PM UTC",
	 *   "updateDate" : "2017-11-01 15:24 PM UTC",
	 *   "productUuid" : "4d723be5-d932-4f91-b8e7-ea6a030098d4",
	 *   "name" : "Product3",
	 *   "shortDescription" : "product3 short description",
	 *   "longDescription" : "product3 long description"
	 * }, { ... }
	 * ]
     * @param recommendation
     */
    // @formatter:on
	@RequestMapping(method = RequestMethod.POST, value = "/recommendation")
	public Recommendation saveRecommendation(@RequestBody Recommendation recommendation) {

		return recommendationService.saveRecommendation(recommendation);
	}

}
