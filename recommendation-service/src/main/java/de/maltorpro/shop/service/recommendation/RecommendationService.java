package de.maltorpro.shop.service.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.maltorpro.shop.model.Recommendation;


@RestController
public class RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

    /**
     * Sample usage: curl $HOST:$PORT/recommendation?productId=1
     *
     * @param productId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/recommendation/{productId}")
    public List<Recommendation> getRecommendations(@PathVariable int productId) {


        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1"));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 2"));
        list.add(new Recommendation(productId, 3, "Author 3", 3, "Content 3"));

        LOG.debug("/recommendation response size: {}", list.size());

        return list;
    }
}
