package de.maltorpro.shop.service.review.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.Review;
import de.maltorpro.shop.service.review.ReviewRepository;
import de.maltorpro.shop.service.review.ReviewServiceApplication;
import de.maltorpro.shop.service.test.support.FieldDescription;
import de.maltorpro.shop.service.test.support.ProductTestRepository;
import de.maltorpro.shop.service.test.support.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReviewServiceApplication.class)
@WebAppConfiguration
@EnableJpaRepositories({ "de.maltorpro.shop.service.test.support", "de.maltorpro.shop.service.review" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicaitonTest {

	private MockMvc mockMvc;

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("../documentation/asciidoc/snippets");

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ProductTestRepository produtTestRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private static long defaultReviewId = 1000l;

	private static int reviewCounter = 0;

	private static boolean setUpIsDone = false;

	private static Product product1;
	private static Product product2;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)).build();

		if (!setUpIsDone) {

			product1 = new Product();
			product1.setName("Product1");
			product1.setShortDescription("product1 short description");
			product1.setLongDescription("product1 long description");
			product1.setProductUuid(UUID.randomUUID().toString());

			product2 = new Product();
			product2.setName("Product2");
			product2.setShortDescription("product2 short description");
			product2.setLongDescription("product2 long description");
			product2.setProductUuid(UUID.randomUUID().toString());

			Review review1 = new Review();
			review1.setAuthor("Max Mustermann");
			review1.setReviewText("Nice product");
			review1.setRating(5);
			review1.setProduct(product1);
			review1.setReviewUuid(UUID.randomUUID().toString());

			Review review2 = new Review();
			review2.setAuthor("John Doe");
			review2.setReviewText("Bad product");
			review2.setRating(1);
			review2.setProduct(product1);
			review2.setReviewUuid(UUID.randomUUID().toString());

			produtTestRepository.save(product1);
			produtTestRepository.save(product2);

			reviewRepository.save(review1);
			reviewRepository.save(review2);
			reviewCounter++;
			reviewCounter++;

			setUpIsDone = true;

		}
	}

	@Test
	public void test1SaveReview() throws Exception {

		Review review = new Review();
		review.setAuthor("Max Mustermann2");
		review.setReviewText("Nice product2");
		review.setRating(3);
		review.setProduct(product2);

		this.mockMvc
				.perform(post("/review").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(review)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.author", equalTo("Max Mustermann2")))
				.andExpect(jsonPath("$.reviewText", equalTo("Nice product2")))
				.andExpect(jsonPath("$.rating", equalTo(3))).andExpect(jsonPath("$.product.name", equalTo("Product2")))
				.andExpect(jsonPath("$.product.shortDescription", equalTo("product2 short description")))
				.andExpect(jsonPath("$.product.longDescription", equalTo("product2 long description")))
				.andDo(document("review-save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						requestFields(
								attributes(key("title").value(FieldDescription.REQUEST_FIELDS)),
								fieldWithPath("creationDate").description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("updateDate").description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("reviewId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("reviewUuid").description(FieldDescription.REVIEW_UUID_DESCRIPTION),
								fieldWithPath("author").description(FieldDescription.REVIEW_AUTHOR_DESCRIPTION),
								fieldWithPath("rating").description(FieldDescription.REVIEW_RATING_DESCRIPTION),
								fieldWithPath("reviewText").description(FieldDescription.REVIEW_REVIEW_TEXT),
								fieldWithPath("product").description(FieldDescription.REVIEWP_RODUCT_DESCRIPTION))));
		reviewCounter++;
	}

	@Test
	public void test2GetReviewsForProduct() throws Exception {

		this.mockMvc
				.perform(get("/reviews/product/{uuid}/{page}/{size}", product1.getProductUuid(), 0, 4)
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content[*]", hasSize(2)))
				.andExpect(jsonPath("$.content[0].author", equalTo("Max Mustermann")))
				.andExpect(jsonPath("$.content[0].reviewText", equalTo("Nice product")))
				.andExpect(jsonPath("$.content[0].rating", equalTo(5)))
				.andExpect(jsonPath("$.content[0].product.name", equalTo("Product1")))
				.andExpect(jsonPath("$.content[0].product.shortDescription", equalTo("product1 short description")))
				.andExpect(jsonPath("$.content[0].product.longDescription", equalTo("product1 long description")))
				.andDo(document("reviews-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION),
								parameterWithName("page").description(FieldDescription.PAGE_PARAM_DESCRIPTION),
								parameterWithName("size").description(FieldDescription.SIZE_DESCRIPTION)),
						
						responseFields(
								attributes(key("title").value(FieldDescription.RESPONSE_PARAM_DESCRIPTION)),
								fieldWithPath("content[].creationDate")
										.description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("content[].updateDate")
										.description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("content[].reviewId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("content[].reviewUuid")
										.description(FieldDescription.REVIEW_UUID_DESCRIPTION),
								fieldWithPath("content[].author").description(FieldDescription.REVIEW_AUTHOR_DESCRIPTION),
								fieldWithPath("content[].rating").description(FieldDescription.REVIEW_RATING_DESCRIPTION),
								fieldWithPath("content[].reviewText").description(FieldDescription.REVIEW_REVIEW_TEXT),
								fieldWithPath("content[].product")
										.description(FieldDescription.REVIEWP_RODUCT_DESCRIPTION),
								fieldWithPath("totalPages").description(FieldDescription.TOTAL_PAGES_DESCRIPTION),
								fieldWithPath("totalElements").description(FieldDescription.TOTAL_ELEMENTS_DESCRIPTION),
								fieldWithPath("last").description(FieldDescription.LAST_DESCRIPTION),
								fieldWithPath("size").description(FieldDescription.SIZE_DESCRIPTION),
								fieldWithPath("number").description(FieldDescription.NUMBER_DESCRIPTION),
								fieldWithPath("sort").description(FieldDescription.SORT_DESCRIPTION),
								fieldWithPath("first").description(FieldDescription.FIRST_DESCRIPTION),
								fieldWithPath("numberOfElements")
										.description(FieldDescription.NUMBER_OF_ELEMENTS_DESCRIPTION))));
	}

	@Test
	public void test3UpdateReview() throws Exception {

		Optional<Review> reviewOpt = reviewRepository.findById(defaultReviewId);
		
		assertTrue(reviewOpt.isPresent());
        Review review = reviewOpt.get();
		
		review.setAuthor("Max Mustermann updated");
		review.setReviewText("Nice product updated");
		review.setRating(4);

		this.mockMvc
				.perform(post("/review").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(review)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.author", equalTo("Max Mustermann updated")))
				.andExpect(jsonPath("$.reviewText", equalTo("Nice product updated")))
				.andExpect(jsonPath("$.rating", equalTo(4))).andExpect(jsonPath("$.product.name", equalTo("Product1")))
				.andExpect(jsonPath("$.product.shortDescription", equalTo("product1 short description")))
				.andExpect(jsonPath("$.product.longDescription", equalTo("product1 long description")));
	}

	@Test
	public void test4DeleteReview() throws Exception {

		assertEquals("Check the review count before delete.", reviewCounter, reviewRepository.count());

		Optional<Review> reviewOpt = reviewRepository.findById(defaultReviewId);
		
	    assertTrue(reviewOpt.isPresent());
	    Review review = reviewOpt.get();
		
		this.mockMvc
				.perform(delete("/review/{uuid}", review.getReviewUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("1")))
				.andDo(document("review-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.REVIEW_UUID_DESCRIPTION))
						
						));

		reviewCounter--;
		assertEquals("Check the review count after delete.", reviewCounter, reviewRepository.count());
	}
}
