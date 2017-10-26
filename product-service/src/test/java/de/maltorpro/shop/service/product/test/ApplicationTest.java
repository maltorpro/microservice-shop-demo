package de.maltorpro.shop.service.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.service.product.ProductRepository;
import de.maltorpro.shop.service.product.ProductServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@WebAppConfiguration
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

	/*
	 * Product field descriptions
	 */

	private static final String productUuidDescription = "The unique identifier of the product";

	private static final String nameDescription = "Name of the product";

	private static final String shortDescription = "Short product description";

	private static final String longDescription = "Long product description";

	private static final String linksDescription = "Link to the next product";

	private static final String notUsedResponse = "Not used, only for the response!";

	/*
	 * Page field descriptions
	 */

	private static final String totalElementsDescription = "Total amount of elements.";

	private static final String totalPagesDescription = "Number of total pages.";

	private static final String lastDescription = "Indicates whether the current page is the first one.";

	private static final String sizeDescription = "Size of the page.";

	private static final String numberDescription = "Number of the current page.";

	private static final String sortDescription = "Sorting parameters for the page.";

	private static final String firstDescription = "Indicates whether the current page is the first one.";

	private static final String numberOfElementsDescription = "Number of elements currently on this page.";

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

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testSaveDefaultProduct() throws Exception {

		Product product = new Product();
		product.setName("Product2");
		product.setShortDescription("product2 short description");
		product.setLongDescription("product2 long description");

		this.mockMvc
				.perform(post("/product").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(asJsonString(product)))
				.andDo(print()).andExpect(status().isOk())
				.andDo(document("product-save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),

						requestFields(attributes(key("title").value("Fields for saving a product")),
								fieldWithPath("productUuid").description(productUuidDescription),
								fieldWithPath("name").description(nameDescription),
								fieldWithPath("shortDescription").description(shortDescription),
								fieldWithPath("longDescription").description(longDescription),
								fieldWithPath("links").description(notUsedResponse))));

		productCounter++;
	}

	@Test
	public void testGetDefaultProdut() throws Exception {

		Product product = productRepository.findOne(defaultProductId);

		this.mockMvc.perform(get("/product/" + product.getProductUuid()).accept(MediaTypes.HAL_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo("Product1")))
				.andDo(document("product-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						responseFields(fieldWithPath("productUuid").description(productUuidDescription),
								fieldWithPath("name").description(nameDescription),
								fieldWithPath("shortDescription").description(shortDescription),
								fieldWithPath("longDescription").description(longDescription),
								fieldWithPath("_links.next.href").description(linksDescription))));
	}

	@Test
	public void testUpdateProduct() throws Exception {

		Product product = productRepository.findOne(updateProductId);
		product.setName("name updated");
		product.setShortDescription("short description updated");
		product.setLongDescription("long description updated");

		this.mockMvc
				.perform(post("/product").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
						.content(asJsonString(product)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.productUuid", equalTo(product.getProductUuid())))
				.andExpect(jsonPath("$.name", equalTo("name updated")))
				.andExpect(jsonPath("$.shortDescription", equalTo("short description updated")))
				.andExpect(jsonPath("$.longDescription", equalTo("long description updated")));
	}

	@Test
	public void testDeleteProduct() throws Exception {

		assertEquals("Check the product count before delete.", productCounter, productRepository.count());

		Product product = productRepository.findOne(deleteProductId);

		this.mockMvc
				.perform(delete("/product/" + product.getProductUuid())
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(equalTo("1")))
				.andDo(document("product-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

		productCounter--;
		assertEquals("Check the product count before delete.", productCounter, productRepository.count());
	}

	@Test
	public void testGetProducts() throws Exception {

		this.mockMvc.perform(get("/products/0/3").accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].name", equalTo("Product1")))
				.andDo(document("products-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						responseFields(fieldWithPath("content[].productUuid").description(productUuidDescription),
								fieldWithPath("content[].name").description(nameDescription),
								fieldWithPath("content[].shortDescription").description(shortDescription),
								fieldWithPath("content.[].longDescription").description(longDescription),
								fieldWithPath("content[].links").description(linksDescription),
								fieldWithPath("totalPages").description(totalPagesDescription),
								fieldWithPath("totalElements").description(totalElementsDescription),
								fieldWithPath("last").description(lastDescription),
								fieldWithPath("size").description(sizeDescription),
								fieldWithPath("number").description(numberDescription),
								fieldWithPath("sort").description(sortDescription),
								fieldWithPath("first").description(firstDescription),
								fieldWithPath("numberOfElements").description(numberOfElementsDescription))));
	}

}
