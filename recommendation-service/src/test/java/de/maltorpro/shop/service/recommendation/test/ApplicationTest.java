package de.maltorpro.shop.service.recommendation.test;

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

import java.util.ArrayList;
import java.util.List;
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
import de.maltorpro.shop.model.Recommendation;
import de.maltorpro.shop.service.recommendation.RecommendationRepository;
import de.maltorpro.shop.service.recommendation.RecommendationServiceApplication;
import de.maltorpro.shop.service.test.support.FieldDescription;
import de.maltorpro.shop.service.test.support.ProductTestRepository;
import de.maltorpro.shop.service.test.support.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommendationServiceApplication.class)
@WebAppConfiguration
@EnableJpaRepositories({ "de.maltorpro.shop.service.test.support", "de.maltorpro.shop.service.recommendation" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

	private MockMvc mockMvc;

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("../documentation/asciidoc/snippets");

	@Autowired
	private ProductTestRepository productTestRepository;

	@Autowired
	private RecommendationRepository recommendationRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private static long defaultRecommendationId = 1000l;

	private static int recommendationCounter = 0;

	private static boolean setUpIsDone = false;

	private static Product product1;
	private static Product product2;
	private static Product product3;
	private static Product product4;
	private static Product product5;

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

			product3 = new Product();
			product3.setName("Product3");
			product3.setShortDescription("product3 short description");
			product3.setLongDescription("product3 long description");
			product3.setProductUuid(UUID.randomUUID().toString());

			product4 = new Product();
			product4.setName("Product4");
			product4.setShortDescription("product4 short description");
			product4.setLongDescription("product4 long description");
			product4.setProductUuid(UUID.randomUUID().toString());

			product5 = new Product();
			product5.setName("Product5");
			product5.setShortDescription("product5 short description");
			product5.setLongDescription("product5 long description");
			product5.setProductUuid(UUID.randomUUID().toString());

			productTestRepository.save(product1);
			productTestRepository.save(product2);
			productTestRepository.save(product3);
			productTestRepository.save(product4);
			productTestRepository.save(product5);

			List<Product> recommendedProducts = new ArrayList<>();
			recommendedProducts.add(product1);
			recommendedProducts.add(product3);
			recommendedProducts.add(product4);

			Recommendation recommendation = new Recommendation();
			recommendation.setRecommendationFor(product5);
			recommendation.setRecommendations(recommendedProducts);
			recommendation.setRecommendationUuid(UUID.randomUUID().toString());

			recommendationRepository.save(recommendation);
			recommendationCounter++;
			setUpIsDone = true;
		}
	}

	@Test
	public void test1SaveRecommendation() throws Exception {

		List<Product> recommendedProducts = new ArrayList<>();
		recommendedProducts.add(product2);
		recommendedProducts.add(product3);
		recommendedProducts.add(product4);

		Recommendation recommendation = new Recommendation();
		recommendation.setRecommendationFor(product1);
		recommendation.setRecommendations(recommendedProducts);

		this.mockMvc
				.perform(post("/recommendation").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recommendation)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.recommendations[*]", hasSize(3)))
				.andExpect(jsonPath("$.recommendations[0].name", equalTo("Product2")))
				.andExpect(jsonPath("$.recommendations[0].shortDescription", equalTo("product2 short description")))
				.andExpect(jsonPath("$.recommendations[0].longDescription", equalTo("product2 long description")))
				.andDo(document("recommendation-save", preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()), 
						requestFields(
								attributes(key("title").value(FieldDescription.REQUEST_FIELDS)),
								fieldWithPath("creationDate").description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("updateDate").description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("recommendationId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("recommendationUuid")
										.description(FieldDescription.RECOMMENDATION_UUID_DESCRIPTION),
								fieldWithPath("recommendationFor")
										.description(FieldDescription.RECOMMENDATION_FOR_DESCRIPTION),
								fieldWithPath("recommendations[]")
										.description(FieldDescription.RECOMMENDATIONS_DESCRIPTION)

						)));

		recommendationCounter++;
	}

	@Test
	public void test2GetRecommendationForProduct() throws Exception {

		Optional<Recommendation> recommendationOpt = recommendationRepository.findById(defaultRecommendationId);

		assertTrue(recommendationOpt.isPresent());
		Recommendation recommendation = recommendationOpt.get();
		
		this.mockMvc
				.perform(get("/recommendation/product/{uuid}", recommendation.getRecommendationFor().getProductUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.recommendationFor.productUuid",
						equalTo(recommendation.getRecommendationFor().getProductUuid())))
				.andExpect(jsonPath("$.recommendations[*]", hasSize(3)))
				.andExpect(jsonPath("$.recommendations[1].name", equalTo("Product3")))
				.andExpect(jsonPath("$.recommendations[1].shortDescription", equalTo("product3 short description")))
				.andExpect(jsonPath("$.recommendations[1].longDescription", equalTo("product3 long description")))
				.andDo(document("recommendation-get", preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION)),
						responseFields(
								attributes(key("title").value(FieldDescription.RESPONSE_PARAM_DESCRIPTION)),
								fieldWithPath("creationDate").description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("updateDate").description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("recommendationId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("recommendationUuid")
										.description(FieldDescription.RECOMMENDATION_UUID_DESCRIPTION),
								fieldWithPath("recommendationFor")
										.description(FieldDescription.RECOMMENDATION_FOR_DESCRIPTION),
								fieldWithPath("recommendations[]")
										.description(FieldDescription.RECOMMENDATIONS_DESCRIPTION))));
	}

	@Test
	public void test3UpdateRecommendation() throws Exception {

		Optional<Recommendation> recommendationOpt = recommendationRepository.findById(defaultRecommendationId);
		
		assertTrue(recommendationOpt.isPresent());
        Recommendation recommendation = recommendationOpt.get();
		
		recommendation.getRecommendations().remove(0);

		this.mockMvc
				.perform(post("/recommendation").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recommendation)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.recommendationFor.productUuid",
						equalTo(recommendation.getRecommendationFor().getProductUuid())))
				.andExpect(jsonPath("$.recommendations[*]", hasSize(2)))
				.andExpect(jsonPath("$.recommendations[0].name", equalTo("Product3")))
				.andExpect(jsonPath("$.recommendations[0].shortDescription", equalTo("product3 short description")))
				.andExpect(jsonPath("$.recommendations[0].longDescription", equalTo("product3 long description")));
	}

	@Test
	public void test4DeleteRecommendationByProduct() throws Exception {

		assertEquals("Check the recommendation count before delete.", recommendationCounter,
				recommendationRepository.count());

		Optional<Recommendation> recommendationOpt = recommendationRepository.findById(defaultRecommendationId);
		
		assertTrue(recommendationOpt.isPresent());
        Recommendation recommendation = recommendationOpt.get();
		
		this.mockMvc
				.perform(delete("/recommendation/product/{uuid}", recommendation.getRecommendationFor().getProductUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("1"))).andDo(document(
						"recommendation-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION))));

		recommendationCounter--;
		assertEquals("Check the product count after delete.", recommendationCounter, recommendationRepository.count());
	}
}
