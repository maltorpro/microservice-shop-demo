package de.maltorpro.shop.service.recommendation;

import java.util.Date;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import de.maltorpro.shop.model.Recommendation;

@Service("recommendationService")
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

	@Autowired
	private RecommendationRepository recommendationRepository;

	@Override
	public Recommendation saveRecommendation(Recommendation recommendation) {

		if (recommendation == null) {
			throw new IllegalArgumentException("recommendation cannot be null");
		}
		Recommendation recommendationDB = null;

		if (!StringUtils.isEmpty(recommendation.getRecommendationUuid())) {
			recommendationDB = recommendationRepository
					.findFirstByRecommendationUuid(recommendation.getRecommendationUuid());
		}

		// set uuid for the new recommendation
		if (recommendationDB == null) {
			recommendationDB = recommendation;
			recommendationDB.setRecommendationUuid(UUID.randomUUID().toString());
		} else { // update recommendation

			recommendationDB.setUpdateDate(new Date());
			recommendationDB.setRecommendationFor(recommendation.getRecommendationFor());
			recommendationDB.setRecommendations(recommendation.getRecommendations());
		}

		return recommendationRepository.save(recommendationDB);
	}

	@Override
	public long deleteByProductUuid(String productUuid) {
		return recommendationRepository.deleteByRecommendationForProductUuid(productUuid);
	}

	@Override
	public Recommendation findByProductUuid(String productUuid) {
		return recommendationRepository.findFirstByRecommendationForProductUuid(productUuid);
	}

}
