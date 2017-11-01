package de.maltorpro.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(name = "IDX_REVIEW_IDS", columnList = "reviewId,reviewUuid") })
public class Review extends ShopObject {
	// @formatter:off
	
	@Id
	@GenericGenerator(
			name = "reviewSequenceGenerator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "REVEIW_SEQUENCE"),
					@Parameter(name = "initial_value", value = "1000"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "reviewSequenceGenerator")
	@JsonIgnore
	private Long reviewId;

	@Column(columnDefinition = "char(36)", nullable = false)
	private String reviewUuid;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private Integer rating;

	@OneToOne(optional = false)
	private Product product;

	// @formatter:on
	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reviewUuid == null) ? 0 : reviewUuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		if (reviewUuid == null) {
			if (other.reviewUuid != null)
				return false;
		} else if (!reviewUuid.equals(other.reviewUuid))
			return false;
		return true;
	}

}
