package de.maltorpro.shop.service.review;

import org.springframework.data.domain.Page;

import de.maltorpro.shop.model.Review;

public interface ReviewService {

	Review saveReview(Review review);

	long deleteByUuid(String uuid);

	Page<Review> findByProductUuid(String productUuid, int page, int size);
}
