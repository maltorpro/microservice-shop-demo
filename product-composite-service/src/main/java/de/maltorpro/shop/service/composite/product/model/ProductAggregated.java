package de.maltorpro.shop.service.composite.product.model;

import java.util.List;
import java.util.stream.Collectors;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;

public class ProductAggregated {

    private ProductSummary product;
    private List<RecommendationSummary> recommendations;
    private List<ReviewSummary> reviews;

    public ProductAggregated(Product product,
            List<Recommendation> recommendations, List<Review> reviews) {

        // 1. Set product
        this.product = new ProductSummary(product.getProductUuid(),
                product.getName());

        // 2. Copy summary recommendation info, if available
        if (recommendations != null)
            this.recommendations = recommendations.stream()
                    .map(x -> new RecommendationSummary(
                            x.getRecommendationUuid(), x.getRecommendations()))
                    .collect(Collectors.toList());

        // 3. Set reviews
        if (reviews != null) {
            this.reviews = reviews.stream().map(x -> {

                ReviewSummary reviewSummary = new ReviewSummary();
                reviewSummary.setReviewUuid(x.getReviewUuid());
                reviewSummary.setAuthor(x.getAuthor());
                reviewSummary.setRating(x.getRating());
                return reviewSummary;

            }).collect(Collectors.toList());
        }

    }

    public List<RecommendationSummary> getRecommendations() {
        return recommendations;
    }

    public List<ReviewSummary> getReviews() {
        return reviews;
    }

    public ProductSummary getProduct() {
        return product;
    }
}
