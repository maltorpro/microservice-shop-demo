package de.maltorpro.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(name = "IDX_PRODUCT_IDS", columnList = "productId,productUuid") })
public class Product extends ShopObject {
	// @formatter:off
	
	@Id
	@GenericGenerator(
	        name = "productSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "PRODUCT_SEQUENCE"),
	                @Parameter(name = "initial_value", value = "1000"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@GeneratedValue(generator = "productSequenceGenerator")
	@JsonIgnore
	private Long productId;

	@Column(columnDefinition = "char(36)", nullable = false)
	private String productUuid;

	@Column(nullable = false)
	private String name;

	private String shortDescription;

	@Column(length = 1024)
	private String longDescription;

	// @formatter:on

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productUuid=" + productUuid + ", name=" + name
				+ ", shortDescription=" + shortDescription + ", longDescription=" + longDescription + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productUuid == null) ? 0 : productUuid.hashCode());
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
		Product other = (Product) obj;
		if (productUuid == null) {
			if (other.productUuid != null)
				return false;
		} else if (!productUuid.equals(other.productUuid))
			return false;
		return true;
	}

}
