package de.maltorpro.shop.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(indexes = {
        @Index(name = "IDX_RECOMMENDATION_IDS", columnList = "recommendationId,recommendationUuid") })
public class Recommendation extends ShopObject {
    // @formatter:off

    @Id
    @GenericGenerator(name = "recommendationSequenceGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "RECOMMENDATION_SEQUENCE"),
            @Parameter(name = "initial_value", value = "1000"),
            @Parameter(name = "increment_size", value = "1") })
    @GeneratedValue(generator = "recommendationSequenceGenerator")
    private Long recommendationId;

    @Column(columnDefinition = "char(36)", nullable = false)
    private String recommendationUuid;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product recommendationFor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recommenation_product", joinColumns = @JoinColumn(name = "recommendationId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> recommendations;
    // @formatter:on

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getRecommendationUuid() {
        return recommendationUuid;
    }

    public void setRecommendationUuid(String recommendationUuid) {
        this.recommendationUuid = recommendationUuid;
    }

    public Product getRecommendationFor() {
        return recommendationFor;
    }

    public void setRecommendationFor(Product recommendationFor) {
        this.recommendationFor = recommendationFor;
    }

    public List<Product> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Product> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((recommendationUuid == null) ? 0
                : recommendationUuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recommendation other = (Recommendation) obj;
        if (recommendationUuid == null) {
            if (other.recommendationUuid != null)
                return false;
        } else if (!recommendationUuid.equals(other.recommendationUuid))
            return false;
        return true;
    }

}
