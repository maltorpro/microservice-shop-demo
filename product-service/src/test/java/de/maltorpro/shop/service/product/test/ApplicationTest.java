package de.maltorpro.shop.service.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)).build();

		Product product1 = new Product();
		product1.setName("Product1");
		product1.setShortDescription("product1 short description");
		product1.setLongDescription("product1 long description");
		product1.setProductUuid(UUID.randomUUID().toString());

		this.productRepository.deleteAll();
		this.productRepository.save(product1);
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
				.andDo(print()).andExpect(status().isOk()).andDo(document("product-save",

						requestFields(attributes(key("title").value("Fields saving a product")),
								fieldWithPath("productUuid").description("The unique prodcut identifier"),
								fieldWithPath("name").description("Name of the product"),
								fieldWithPath("shortDescription").description("Short product description"),
								fieldWithPath("longDescription").description("Long product description"),
								fieldWithPath("links").description("Not used, only for the response!"))));
	}

	@Test
	public void testGetDefaultProdut() throws Exception {

		Product product = productRepository.findOne(1000l);

		this.mockMvc.perform(get("/product/" + product.getProductUuid()).accept(MediaTypes.HAL_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo("Product1")))
				.andDo(document("product-get",
						responseFields(fieldWithPath("productUuid").description("The unique identifier of the product"),
								fieldWithPath("name").description("Name of the product"),
								fieldWithPath("shortDescription").description("Short product description"),
								fieldWithPath("longDescription").description("Long product description"),
								fieldWithPath("_links.next.href").description("Link to the next product"))));
	}
}
