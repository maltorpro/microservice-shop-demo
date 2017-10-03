package de.maltorpro.shop.service.product.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;



import de.maltorpro.shop.service.product.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductService.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureRestDocs(outputDir = "../documentation/asciidoc/snippets")
public class WebLayerTest {
	
	@Autowired
    private MockMvc mockMvc;


    @Test
    public void shouldReturnDefaultProdut() throws Exception {
        this.mockMvc.perform(get("/product/1").accept(MediaTypes.HAL_JSON))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", equalTo(1)))
            .andDo(document("product",
            		PayloadDocumentation.responseFields(
        				   PayloadDocumentation.fieldWithPath("productId").description("The unique identifier of the product"),
        				   PayloadDocumentation.fieldWithPath("weight").description("bla"),
        				   PayloadDocumentation.fieldWithPath("name").description("Name of the Product"),
        				   PayloadDocumentation.fieldWithPath("_links.weiter.href").description("bla")
            		)
        	));
    }
}
