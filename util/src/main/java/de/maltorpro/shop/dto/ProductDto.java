package de.maltorpro.shop.dto;

public class ProductDto {

	private String productUuid;
	private String name;
	private String shortDescription;
	private String longDescription;

	public ProductDto() {
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

	@Override
	public String toString() {
		return "ProductResourceDto [productUuid=" + productUuid + ", name=" + name + ", shortDescription="
				+ shortDescription + ", longDescription=" + longDescription + "]";
	}

}
