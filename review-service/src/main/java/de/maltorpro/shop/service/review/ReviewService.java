package de.maltorpro.shop.service.review;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Review;

@RestController
public class ReviewService {

	/**
	 * Sample usage: curl $HOST:$PORT/review?productId=1
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/review/{productId}")
	public List<Review> getReviews(@PathVariable int productId) {

		List<Review> list = new ArrayList<>();
		list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1"));
		list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2"));
		list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3"));

		return list;
	}
}
