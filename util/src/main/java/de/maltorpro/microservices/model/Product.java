package de.maltorpro.microservices.model;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

//@Relation()
public class Product extends ResourceSupport  {
    private int productId;
    private String name;
    private int weight;

    public Product() {

    }
    
    public Product(int productId, String name, int weight) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
