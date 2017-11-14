package de.maltorpro.shop.service.test.support;

public final class FieldDescription {

	/*
	 * Table titles
	 */

	public static final String requestParamDescription = "Request path parameters";

	public static final String responseParamDescription = "Response fields";

	/*
	 * Paging path parameters
	 */

	public static final String pageParamDescription = "Page of results";

	public static final String sizeParamDescription = "Size of results";

	/*
	 * ShopOpject field descriptions
	 */

	public static final String creationDateDescription = "Creation date ot the object";

	public static final String updateDateDescription = "Update date of the object";

	public static final String idDescription = "Database primary key";

	/*
	 * Product field descriptions
	 */

	public static final String productUuidDescription = "The unique identifier of the product";

	public static final String nameDescription = "Name of the product";

	public static final String shortDescription = "Short product description";

	public static final String longDescription = "Long product description";

	public static final String linksDescription = "Link to the next product";

	/*
	 * Recommendation field descriptions
	 */

	public static final String recommendationUuidDescription = "The unique identifier of the recommendation";

	public static final String recommendationForDescription = "Product for which the recommendations are";

	public static final String recommendationsDescription = "Recommendations for the product";

	/*
	 * Review field descriptions
	 */

	public static final String reviewUuidDescription = "The unique identifier of the review";

	public static final String reviewAuthorDescription = "Author of the rating";

	public static final String reviewRatingDescription = "Evaluation of the product in a value range from 1 to 5 stars";

	public static final String reviewReviewText = "Review text written by the author";

	public static final String reviewProductDescription = "Rated product";

	/*
	 * Page field descriptions
	 */

	public static final String totalElementsDescription = "Total amount of elements.";

	public static final String totalPagesDescription = "Number of total pages.";

	public static final String lastDescription = "Indicates whether the current page is the first one.";

	public static final String sizeDescription = "Size of the page.";

	public static final String numberDescription = "Number of the current page.";

	public static final String sortDescription = "Sorting parameters for the page.";

	public static final String firstDescription = "Indicates whether the current page is the first one.";

	public static final String numberOfElementsDescription = "Number of elements currently on this page.";

	public FieldDescription() {
		// not for creation.
	}
}
