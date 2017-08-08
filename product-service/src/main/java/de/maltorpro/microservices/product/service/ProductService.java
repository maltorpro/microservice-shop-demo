package de.maltorpro.microservices.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.microservices.model.Product;
import de.maltorpro.microservices.util.SetProcTimeBean;

import static net.logstash.logback.marker.Markers.*;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;




@RestController
@RefreshScope
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Value("${message:Hello default}")
    private String message;

    @Autowired
    private SetProcTimeBean setProcTimeBean;
    
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

        int pt = setProcTimeBean.calculateProcessingTime();
        LOG.info("/product called, processing time: {}", pt);

        sleep(pt);

        LOG.debug("/product return the found product");
        
        Product product = new Product(productId, "name", 123);
        product.add(linkTo(methodOn(ProductService.class).getProduct(2)).withRel("weiter"));

        
        
        LOG.info(append("dataobject", product), "Die daten meines tollen produktes");
        
        return product;
    }

    private void sleep(int pt) {
        try {
            Thread.sleep(pt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sample usage:
     *
     *  curl "http://localhost:10002/set-processing-time?minMs=1000&maxMs=2000"
     *
     * @param minMs
     * @param maxMs
     */
    @RequestMapping("/set-processing-time")
    public void setProcessingTime(
            @RequestParam(value = "minMs", required = true) int minMs,
            @RequestParam(value = "maxMs", required = true) int maxMs) {

        LOG.info("/set-processing-time called: {} - {} ms", minMs, maxMs);

        setProcTimeBean.setDefaultProcessingTime(minMs, maxMs);
    }
}
