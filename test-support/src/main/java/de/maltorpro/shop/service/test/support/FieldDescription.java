package de.maltorpro.shop.service.test.support;

public final class FieldDescription {

	/*
	 * Table titles
	 */

	public static final String REQUEST_PARAM_DESCRIPTION = "Request path parameters";

	public static final String RESPONSE_PARAM_DESCRIPTION = "Response fields";

	/*
	 * Paging path parameters
	 */

	public static final String PAGE_PARAM_DESCRIPTION = "Page of results";

	public static final String SIZE_PARAM_DESCRIPTION = "Size of results";

	/*
	 * ShopOpject field descriptions
	 */

	public static final String CREATION_DATE_DESCRIPTION = "Creation date ot the object";

	public static final String UPDATE_DATE_DESCRIPTION = "Update date of the object";

	public static final String ID_DESCRIPTION = "Database primary key";

	/*
	 * Product field descriptions
	 */

	public static final String PRODUCT_UUID_DESCRIPTION = "The unique identifier of the product";

	public static final String NAME_DESCRIPTION = "Name of the product";

	public static final String SHORT_DESCRIPTION = "Short product description";

	public static final String LONG_DESCRIPTION = "Long product description";

	public static final String linksDescription = "Link to the next product";

	/*
	 * Recommendation field descriptions
	 */

	public static final String RECOMMENDATION_UUID_DESCRIPTION = "The unique identifier of the recommendation";

	public static final String RECOMMENDATION_FOR_DESCRIPTION = "Product for which the recommendations are";

	public static final String RECOMMENDATIONS_DESCRIPTION = "Recommendations for the product";

	/*
	 * Review field descriptions
	 */

	public static final String REVIEW_UUID_DESCRIPTION = "The unique identifier of the review";

	public static final String REVIEW_AUTHOR_DESCRIPTION = "Author of the rating";

	public static final String REVIEW_RATING_DESCRIPTION = "Evaluation of the product in a value range from 1 to 5 stars";

	public static final String REVIEW_REVIEW_TEXT = "Review text written by the author";

	public static final String REVIEWP_RODUCT_DESCRIPTION = "Rated product";

	/*
	 * Page field descriptions
	 */

	public static final String TOTAL_ELEMENTS_DESCRIPTION = "Total amount of elements.";

	public static final String TOTAL_PAGES_DESCRIPTION = "Number of total pages.";

	public static final String LAST_DESCRIPTION = "Indicates whether the current page is the first one.";

	public static final String SIZE_DESCRIPTION = "Size of the page.";

	public static final String NUMBER_DESCRIPTION = "Number of the current page.";

	public static final String SORT_DESCRIPTION = "Sorting parameters for the page.";

	public static final String FIRST_DESCRIPTION = "Indicates whether the current page is the first one.";

	public static final String NUMBER_OF_ELEMENTS_DESCRIPTION = "Number of elements currently on this page.";

	private FieldDescription() {
		// not for creation.
	}
}
