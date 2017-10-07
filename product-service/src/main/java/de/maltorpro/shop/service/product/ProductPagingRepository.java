package de.maltorpro.shop.service.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.maltorpro.shop.model.Product;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product,Integer> {

}
