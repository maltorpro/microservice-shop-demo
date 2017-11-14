package de.maltorpro.shop.service.composite.product.model;

import java.util.List;

import de.maltorpro.shop.model.Product;

public class RecommendationSummary {

	private String recommendationUuid;
	private List<Product> recommendations;

	public RecommendationSummary(String recommendationUuid, List<Product> recommendations) {
		this.recommendationUuid = recommendationUuid;
		this.recommendations = recommendations;
	}

	public String getRecommendationUuid() {
		return recommendationUuid;
	}

	public List<Product> getRecommendations() {
		return recommendations;
	}

}
