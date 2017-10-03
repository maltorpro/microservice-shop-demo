package de.maltorpro.shop.service.product;

import static net.logstash.logback.marker.Markers.append;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Product;
import de.maltorpro.shop.model.enums.LogtashMarker;


@RestController
@RefreshScope
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Value("${message:Hello default}")
    private String message;

    
    @RequestMapping("/message")
    String getMessage() {
    	
    	LOG.info("/product called, message: {}", message);
        return this.message;
    }
    

    /**
     * Sample usage: curl $HOST:$PORT/product/1
     *
     * @param productId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/product/{productId}")
    public Product getProduct(@PathVariable int productId) {

    	LOG.info(append(LogtashMarker.EVENT.name(), "dd"), "Call product service");  	
        
        Product product = new Product(productId, "name", 123);
        product.add(linkTo(methodOn(ProductService.class).getProduct(2)).withRel("weiter"));

        LOG.info(append("dataobject", product), "Die daten meines tollen produktes");
        
        return product;
    }
}
