package de.maltorpro.shop.service.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import de.maltorpro.shop.model.Review;

public interface ReviewPagingRepository extends PagingAndSortingRepository<Review, Integer> {

	Page<Review> findByProductProductUuid(String productUuid, Pageable pageable);
}
