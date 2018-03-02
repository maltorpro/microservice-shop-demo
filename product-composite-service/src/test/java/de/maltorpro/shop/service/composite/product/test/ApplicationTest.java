package de.maltorpro.shop.service.composite.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import de.maltorpro.shop.service.composite.product.ProductCompositeServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductCompositeServiceApplication.class, properties = {
        "eureka.client.enabled:false" })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class ApplicationTest {

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
            "../documentation/asciidoc/snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate restTemplate;

    private static boolean setUpIsDone = false;

    private static final String PRODUCT_UUID = "82a66bde-03d5-4c67-9be3-f83557eb2917";

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation
                        .documentationConfiguration(this.restDocumentation))
                .build();

        if (!setUpIsDone) {

            setUpIsDone = true;
        }
    }

    @Test
    @WithMockUser(username = "developer", roles = { "DEVELOPERS" })
    public void test1GetFullProduct() throws Exception {

        // product-service
        MockRestServiceServer mockProductService = MockRestServiceServer
                .createServer(restTemplate);
        mockProductService
                .expect(manyTimes(), requestToUriTemplate(
                        "http://product-service/product/{uuid}", PRODUCT_UUID))
                .andExpect(method(HttpMethod.GET)).andRespond(withSuccess(
                        jsonResource("product"), MediaType.APPLICATION_JSON));

        // recommendation-service
        MockRestServiceServer mockRecommendationService = MockRestServiceServer
                .createServer(restTemplate);
        mockRecommendationService.expect(manyTimes(), requestToUriTemplate(
                "http://recommendation-service/recommendation/product/{uuid}",
                PRODUCT_UUID)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResource("recommendation"),
                        MediaType.APPLICATION_JSON));

        // review-service
        MockRestServiceServer mockReviewService = MockRestServiceServer
                .createServer(restTemplate);
        mockReviewService.expect(manyTimes(), requestToUriTemplate(
                "http://review-service/reviews/product/{uuid}/{page}/{size}",
                PRODUCT_UUID, 0, 10)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResource("review"),
                        MediaType.APPLICATION_JSON));
        // test response
        this.mockMvc
                .perform(get("/{uuid}", PRODUCT_UUID).accept(
                        org.springframework.http.MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Product1")));

        mockProductService.verify();
        mockRecommendationService.verify();
        mockReviewService.verify();

    }

    private static Resource jsonResource(String filename) {
        return new ClassPathResource(filename + ".json", ApplicationTest.class);
    }

}
