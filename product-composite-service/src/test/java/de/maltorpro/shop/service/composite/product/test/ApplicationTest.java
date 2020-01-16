package de.maltorpro.shop.service.composite.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.maltorpro.shop.service.composite.product.ProductCompositeServiceApplication;
import de.maltorpro.shop.service.test.support.FieldDescription;
import de.maltorpro.shop.utils.SSLUtils;

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

    private MockRestServiceServer mockServer;

    private static final String PRODUCT_UUID = "82a66bde-03d5-4c67-9be3-f83557eb2917";

    @BeforeClass
    public static void turnOffSslChecking()
            throws KeyManagementException, NoSuchAlgorithmException {
        String certificateCheck = System.getProperty("certificateCheck");

        if (StringUtils.equals(certificateCheck, "false")
                || StringUtils.equals(certificateCheck, "0")) {

            SSLUtils.turnOffSslChecking();
        }
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation
                        .documentationConfiguration(this.restDocumentation))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        restTemplate.setMessageConverters(Collections.singletonList(converter));

        mockServer = MockRestServiceServer.createServer(restTemplate);

    }

    @Test
    @WithMockUser(username = "developer", roles = { "DEVELOPERS" })
    public void test1GetFullProduct() throws Exception {

        // product-service
        mockServer
                .expect(manyTimes(), requestToUriTemplate(
                        "https://product-service/product/{uuid}", PRODUCT_UUID))
                .andExpect(method(HttpMethod.GET)).andRespond(withSuccess(
                        jsonResource("product"), MediaType.APPLICATION_JSON));

        // recommendation-service
        mockServer.expect(manyTimes(), requestToUriTemplate(
                "https://recommendation-service/recommendation/product/{uuid}",
                PRODUCT_UUID)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResource("recommendation"),
                        MediaType.APPLICATION_JSON));

        // review-service
        mockServer.expect(manyTimes(), requestToUriTemplate(
                "https://review-service/reviews/product/{uuid}/{page}/{size}",
                PRODUCT_UUID, 0, 10)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResource("review"),
                        MediaType.APPLICATION_JSON));
        // test response
        this.mockMvc
                .perform(get("/{uuid}", PRODUCT_UUID).accept(
                        org.springframework.http.MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())

                // test product
                .andExpect(jsonPath("$.product.productUuid",
                        equalTo("f67dc537-d3d5-4184-86bd-10012b0b65d4")))
                .andExpect(jsonPath("$.product.name", equalTo("Product2")))

                // test recommendations
                .andExpect(jsonPath("$.recommendations[*]", hasSize(1)))
                .andExpect(jsonPath("$.recommendations[0].products[*]",
                        hasSize(3)))
                .andExpect(jsonPath("$.recommendations[0].recommendationUuid",
                        equalTo("bcd4ac88-ce90-4c48-ab47-9ec01da00f64")))
                .andExpect(jsonPath(
                        "$.recommendations[0].products[0].productUuid",
                        equalTo("98495b7f-a325-41b1-a31d-fba2c5b8381e")))
                .andExpect(jsonPath("$.recommendations[0].products[0].name",
                        equalTo("Product1")))

                // test review
                .andExpect(jsonPath("$.reviews[*]", hasSize(1)))
                .andExpect(jsonPath("$.reviews[0].reviewUuid",
                        equalTo("4b3bfc88-0f73-48ec-b7ba-f8f8ea0ed86b")))
                .andExpect(jsonPath("$.reviews[0].author",
                        equalTo("Max Mustermann2")))
                .andExpect(jsonPath("$.reviews[0].rating", equalTo(3)))

                // documentation
                .andDo(document("product-composite-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(attributes(key("title").value(
                                FieldDescription.REQUEST_PARAM_DESCRIPTION)),
                                parameterWithName("uuid").description(
                                        FieldDescription.PRODUCT_UUID_DESCRIPTION)),
                        responseFields(attributes(key("title").value(
                                FieldDescription.RESPONSE_PARAM_DESCRIPTION)),

                                // product
                                subsectionWithPath("product").description(
                                        FieldDescription.PRODUCT_COMPOSITE_PRODUCT_DESCRIPTION),

                                // recommendations
                                subsectionWithPath("recommendations[]")
                                        .description(
                                                FieldDescription.RECOMMENDATIONS_DESCRIPTION),

                                // reviews
                                subsectionWithPath("reviews[]").description(
                                        FieldDescription.REVIEW_DESCRIPTION))));

        mockServer.verify();

    }

    private static Resource jsonResource(String filename) {
        return new ClassPathResource(filename + ".json", ApplicationTest.class);
    }

}
