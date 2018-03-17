package de.maltorpro.shop.service.composite.product.model;

import java.util.List;
import java.util.stream.Collectors;

import de.maltorpro.shop.model.Product;

public class RecommendationSummary {

    private String recommendationUuid;
    private List<ProductSummary> products;

    public RecommendationSummary(String recommendationUuid,
            List<Product> products) {
        this.recommendationUuid = recommendationUuid;

        this.products = products.stream()
                .map(x -> new ProductSummary(x.getProductUuid(), x.getName()))
                .collect(Collectors.toList());
    }

    public String getRecommendationUuid() {
        return recommendationUuid;
    }

    public List<ProductSummary> getProducts() {
        return products;
    }

}
