package de.maltorpro.shop.service.review;

import org.springframework.data.repository.CrudRepository;

import de.maltorpro.shop.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {

	Review findFirstByReviewUuid(String reviewUuid);

	long deleteByReviewUuid(String reviewUuid);
}
