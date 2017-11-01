package de.maltorpro.shop.service.recommendation;

import de.maltorpro.shop.model.Recommendation;

public interface RecommendationService {

	Recommendation saveRecommendation(Recommendation recommendation);

	long deleteByProductUuid(String productUuid);

	Recommendation findByProductUuid(String productUuid);
}
