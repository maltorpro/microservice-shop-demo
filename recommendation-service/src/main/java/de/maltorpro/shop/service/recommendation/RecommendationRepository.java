package de.maltorpro.shop.service.recommendation;

import org.springframework.data.repository.CrudRepository;

import de.maltorpro.shop.model.Recommendation;

public interface RecommendationRepository extends CrudRepository<Recommendation, Long> {

	long deleteByRecommendationForProductUuid(String productUuid);

	Recommendation findFirstByRecommendationUuid(String recommendationUuid);

	Recommendation findFirstByRecommendationForProductUuid(String productUuid);
}
