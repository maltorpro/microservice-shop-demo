package de.maltorpro.shop.service.review;

import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.maltorpro.shop.model.Review;

@Service("reviewService")
@Transactional
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ReviewPagingRepository reviewPagingRepository;

	@Override
	public Review saveReview(Review review) {

		if (review == null) {
			throw new IllegalArgumentException("The passed review object cannot be null");
		}

		if (review.getProduct() == null) {
			throw new IllegalArgumentException("A review must be related to a product");
		}

		if (review.getProduct().getProductUuid() == null) {
			throw new IllegalArgumentException("Missing uuid field of the product");
		}

		Review reviewDB = null;

		if (!StringUtils.isEmpty(review.getReviewUuid())) {
			reviewDB = reviewRepository.findFirstByReviewUuid(review.getReviewUuid());
		}

		// set uuid for the new review
		if (reviewDB == null) {

			reviewDB = review;
			reviewDB.setReviewUuid(UUID.randomUUID().toString());
		} else { // update review

			reviewDB.setUpdateDate(new Date());
			reviewDB.setAuthor(review.getAuthor());
			reviewDB.setRating(review.getRating());
			reviewDB.setReviewText(review.getReviewText());
		}

		return reviewRepository.save(reviewDB);
	}

	@Override
	public Page<Review> findByProductUuid(String productUuid, int page, int size) {
		return reviewPagingRepository.findByProductProductUuid(productUuid, PageRequest.of(page, size));
	}

	@Override
	public long deleteByUuid(String uuid) {

		return reviewRepository.deleteByReviewUuid(uuid);
	}

}
