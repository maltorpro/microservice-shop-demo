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
	private List<ReviewSummary> reviews;

	public ProductAggregated(Product product, List<Recommendation> recommendations, List<Review> reviews) {

		// 1. Setup product info
		this.name = product.getName();

		// 2. Copy summary recommendation info, if available
		if (recommendations != null)
			this.recommendations = recommendations.stream()
					.map(x -> new RecommendationSummary(x.getRecommendationUuid(), x.getRecommendations()))
					.collect(Collectors.toList());

		// 3. Copy summary review info, if available
		if (reviews != null)
			this.reviews = reviews.stream().map(x -> {
				ReviewSummary rSummary = new ReviewSummary();
				rSummary.setReviewUuid(x.getReviewUuid());
				rSummary.setAuthor(x.getAuthor());
				rSummary.setRating(x.getRating());
				rSummary.setReviewText(x.getReviewText());
				return rSummary;
			}).collect(Collectors.toList());
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

	public List<ReviewSummary> getReviews() {
		return reviews;
	}
}
