package de.maltorpro.shop.service.composite.product;

import org.springframework.http.ResponseEntity;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.model.Review;

public interface ProductCompositeService {

	ResponseEntity<Product> getProduct(int productId);

	ResponseEntity<Recommendation[]> getRecommendations(int productId);

	ResponseEntity<Review[]> getReviews(int productId);
}
