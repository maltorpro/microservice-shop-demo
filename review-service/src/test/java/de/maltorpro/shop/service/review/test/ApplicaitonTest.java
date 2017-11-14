package de.maltorpro.shop.service.review.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
import de.maltorpro.shop.service.product.ProductRepository;
import de.maltorpro.shop.service.review.ReviewRepository;
import de.maltorpro.shop.service.review.ReviewServiceApplication;
import de.maltorpro.shop.service.test.support.FieldDescription;
import de.maltorpro.shop.service.test.support.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReviewServiceApplication.class)
@WebAppConfiguration
@EnableJpaRepositories({ "de.maltorpro.shop.service.product", "de.maltorpro.shop.service.review" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicaitonTest {

	private MockMvc mockMvc;

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("../documentation/asciidoc/snippets");

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ProductRepository produtRepository;

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

			produtRepository.save(product1);
			produtRepository.save(product2);

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
								fieldWithPath("creationDate").description(FieldDescription.creationDateDescription),
								fieldWithPath("updateDate").description(FieldDescription.updateDateDescription),
								fieldWithPath("reviewId").description(FieldDescription.idDescription),
								fieldWithPath("reviewUuid").description(FieldDescription.reviewUuidDescription),
								fieldWithPath("author").description(FieldDescription.reviewAuthorDescription),
								fieldWithPath("rating").description(FieldDescription.reviewRatingDescription),
								fieldWithPath("reviewText").description(FieldDescription.reviewReviewText),
								fieldWithPath("product").description(FieldDescription.reviewProductDescription))));
		reviewCounter++;
	}

	@Test
	public void test2GetReviewsForProduct() throws Exception {

		this.mockMvc
				.perform(get("/reviews/product/" + product1.getProductUuid() + "/0/4")
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content[*]", hasSize(2)))
				.andExpect(jsonPath("$.content[0].author", equalTo("Max Mustermann")))
				.andExpect(jsonPath("$.content[0].reviewText", equalTo("Nice product")))
				.andExpect(jsonPath("$.content[0].rating", equalTo(5)))
				.andExpect(jsonPath("$.content[0].product.name", equalTo("Product1")))
				.andExpect(jsonPath("$.content[0].product.shortDescription", equalTo("product1 short description")))
				.andExpect(jsonPath("$.content[0].product.longDescription", equalTo("product1 long description")))
				.andDo(document("reviews-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("content[].creationDate")
										.description(FieldDescription.creationDateDescription),
								fieldWithPath("content[].updateDate")
										.description(FieldDescription.updateDateDescription),
								fieldWithPath("content[].reviewId").description(FieldDescription.idDescription),
								fieldWithPath("content[].reviewUuid")
										.description(FieldDescription.reviewUuidDescription),
								fieldWithPath("content[].author").description(FieldDescription.reviewAuthorDescription),
								fieldWithPath("content[].rating").description(FieldDescription.reviewRatingDescription),
								fieldWithPath("content[].reviewText").description(FieldDescription.reviewReviewText),
								fieldWithPath("content[].product")
										.description(FieldDescription.reviewProductDescription),
								fieldWithPath("totalPages").description(FieldDescription.totalPagesDescription),
								fieldWithPath("totalElements").description(FieldDescription.totalElementsDescription),
								fieldWithPath("last").description(FieldDescription.lastDescription),
								fieldWithPath("size").description(FieldDescription.sizeDescription),
								fieldWithPath("number").description(FieldDescription.numberDescription),
								fieldWithPath("sort").description(FieldDescription.sortDescription),
								fieldWithPath("first").description(FieldDescription.firstDescription),
								fieldWithPath("numberOfElements")
										.description(FieldDescription.numberOfElementsDescription))));
	}

	@Test
	public void test3UpdateReview() throws Exception {

		Review review = reviewRepository.findOne(defaultReviewId);
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

		Review review = reviewRepository.findOne(defaultReviewId);

		this.mockMvc
				.perform(delete("/review/" + review.getReviewUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("1")))
				.andDo(document("review-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

		reviewCounter--;
		assertEquals("Check the review count after delete.", reviewCounter, reviewRepository.count());
	}
}
