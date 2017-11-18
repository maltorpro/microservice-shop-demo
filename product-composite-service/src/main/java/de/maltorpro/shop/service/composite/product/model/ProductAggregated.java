package de.maltorpro.shop.service.composite.product.model;

import java.util.List;
import java.util.stream.Collectors;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;

public class ProductAggregated {

	private String productUuid;
	private String name;
	private List<RecommendationSummary> recommendations;
	private List<Review> reviews;

	public ProductAggregated(Product product, List<Recommendation> recommendations, List<Review> reviews) {

		// 1. Setup product info
		this.name = product.getName();

		// 2. Copy summary recommendation info, if available
		if (recommendations != null)
			this.recommendations = recommendations.stream()
					.map(x -> new RecommendationSummary(x.getRecommendationUuid(), x.getRecommendations()))
					.collect(Collectors.toList());

		// 3. Set reviews
		this.reviews = reviews;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public String getName() {
		return name;
	}

	public List<RecommendationSummary> getRecommendations() {
		return recommendations;
	}

	public List<Review> getReviews() {
		return reviews;
	}
}
