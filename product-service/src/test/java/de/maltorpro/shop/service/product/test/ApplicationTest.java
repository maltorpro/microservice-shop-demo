package de.maltorpro.shop.service.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.service.product.ProductRepository;
import de.maltorpro.shop.service.product.ProductServiceApplication;
import de.maltorpro.shop.service.test.support.FieldDescription;
import de.maltorpro.shop.service.test.support.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

	private MockMvc mockMvc;

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("../documentation/asciidoc/snippets");

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private static long defaultProductId = 1000l;

	private static long updateProductId = 1001l;

	private static long deleteProductId = 1002l;

	private static int productCounter = 0;

	private static boolean setUpIsDone = false;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)).build();

		if (!setUpIsDone) {

			Product defaultProduct = new Product();
			defaultProduct.setName("Product1");
			defaultProduct.setShortDescription("product1 short description");
			defaultProduct.setLongDescription("product1 long description");
			defaultProduct.setProductUuid(UUID.randomUUID().toString());

			Product updateProduct = new Product();
			updateProduct.setName("Product2");
			updateProduct.setShortDescription("product2 short description");
			updateProduct.setLongDescription("product2 long description");
			updateProduct.setProductUuid(UUID.randomUUID().toString());

			Product deleteProduct = new Product();
			deleteProduct.setName("Product3");
			deleteProduct.setShortDescription("product3 short description");
			deleteProduct.setLongDescription("product3 long description");
			deleteProduct.setProductUuid(UUID.randomUUID().toString());

			this.productRepository.save(defaultProduct);
			this.productRepository.save(updateProduct);
			this.productRepository.save(deleteProduct);

			productCounter = 3;

			setUpIsDone = true;
		}
	}

	@Test
	public void test1SaveProduct() throws Exception {

		Product product = new Product();
		product.setName("Product2");
		product.setShortDescription("product2 short description");
		product.setLongDescription("product2 long description");

		this.mockMvc
				.perform(post("/product").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(product)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo("Product2")))
				.andExpect(jsonPath("$.shortDescription", equalTo("product2 short description")))
				.andExpect(jsonPath("$.longDescription", equalTo("product2 long description")))
				.andDo(document("product-save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),

						requestFields(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								fieldWithPath("productUuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION),
								fieldWithPath("creationDate").description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("updateDate").description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("productId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("name").description(FieldDescription.NAME_DESCRIPTION),
								fieldWithPath("shortDescription").description(FieldDescription.SHORT_DESCRIPTION),
								fieldWithPath("longDescription").description(FieldDescription.LONG_DESCRIPTION))));

		productCounter++;
	}

	@Test
	public void test2GetProdut() throws Exception {

		Product product = productRepository.findOne(defaultProductId);

		this.mockMvc.perform(get("/product/{uuid}", product.getProductUuid()).accept(MediaTypes.HAL_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo("Product1")))
				.andExpect(jsonPath("$.shortDescription", equalTo("product1 short description")))
				.andExpect(jsonPath("$.longDescription", equalTo("product1 long description")))
				.andDo(document("product-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION)),
						responseFields(attributes(key("title").value(FieldDescription.RESPONSE_PARAM_DESCRIPTION)),
								fieldWithPath("productUuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION),
								fieldWithPath("creationDate").description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("updateDate").description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("productId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("name").description(FieldDescription.NAME_DESCRIPTION),
								fieldWithPath("shortDescription").description(FieldDescription.SHORT_DESCRIPTION),
								fieldWithPath("longDescription").description(FieldDescription.LONG_DESCRIPTION),
								fieldWithPath("_links.next.href").description(FieldDescription.linksDescription))));
	}

	@Test
	public void test3UpdateProduct() throws Exception {

		Product product = productRepository.findOne(updateProductId);
		product.setName("name updated");
		product.setShortDescription("short description updated");
		product.setLongDescription("long description updated");

		this.mockMvc
				.perform(post("/product").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(product)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.productUuid", equalTo(product.getProductUuid())))
				.andExpect(jsonPath("$.name", equalTo("name updated")))
				.andExpect(jsonPath("$.shortDescription", equalTo("short description updated")))
				.andExpect(jsonPath("$.longDescription", equalTo("long description updated")));
	}

	@Test
	public void test4DeleteProduct() throws Exception {

		assertEquals("Check the product count before delete.", productCounter, productRepository.count());

		Product product = productRepository.findOne(deleteProductId);

		this.mockMvc
				.perform(delete("/product/{uuid}", product.getProductUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("1")))
				.andDo(document("product-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("uuid").description(FieldDescription.PRODUCT_UUID_DESCRIPTION))

		));

		productCounter--;
		assertEquals("Check the product count after delete.", productCounter, productRepository.count());
	}

	@Test
	public void test5GetProducts() throws Exception {

		this.mockMvc
				.perform(get("/products/{page}/{size}", 0, 3)
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content[*]", hasSize(3)))
				.andExpect(jsonPath("$.content[0].name", equalTo("Product1")))
				.andExpect(jsonPath("$.content[0].shortDescription", equalTo("product1 short description")))
				.andExpect(jsonPath("$.content[0].longDescription", equalTo("product1 long description")))
				.andDo(document("products-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),

						pathParameters(attributes(key("title").value(FieldDescription.REQUEST_PARAM_DESCRIPTION)),
								parameterWithName("page").description(FieldDescription.PAGE_PARAM_DESCRIPTION),
								parameterWithName("size").description(FieldDescription.SIZE_DESCRIPTION)),
						responseFields(attributes(key("title").value(FieldDescription.RESPONSE_PARAM_DESCRIPTION)),
								fieldWithPath("content[].productUuid")
										.description(FieldDescription.PRODUCT_UUID_DESCRIPTION),
								fieldWithPath("content[].creationDate")
										.description(FieldDescription.CREATION_DATE_DESCRIPTION),
								fieldWithPath("content[].updateDate")
										.description(FieldDescription.UPDATE_DATE_DESCRIPTION),
								fieldWithPath("content[].productId").description(FieldDescription.ID_DESCRIPTION),
								fieldWithPath("content[].name").description(FieldDescription.NAME_DESCRIPTION),
								fieldWithPath("content[].shortDescription")
										.description(FieldDescription.SHORT_DESCRIPTION),
								fieldWithPath("content.[].longDescription")
										.description(FieldDescription.LONG_DESCRIPTION),

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

}
