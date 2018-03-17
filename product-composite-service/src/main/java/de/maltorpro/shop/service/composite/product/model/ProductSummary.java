package de.maltorpro.shop.service.composite.product.model;

public class ProductSummary {

    private String productUuid;
    private String name;

    public ProductSummary(String productUuid, String name) {
        this.productUuid = productUuid;
        this.name = name;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
