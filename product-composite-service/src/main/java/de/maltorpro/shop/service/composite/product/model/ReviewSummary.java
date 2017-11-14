package de.maltorpro.shop.service.composite.product.model;

public class ReviewSummary {

	private String reviewUuid;
	private String author;
	private Integer rating;
	private String reviewText;

	public String getReviewUuid() {
		return reviewUuid;
	}

	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

}
