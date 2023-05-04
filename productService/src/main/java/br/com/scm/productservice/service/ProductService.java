package br.com.scm.productservice.service;

import br.com.scm.productservice.model.ProductRequest;
import br.com.scm.productservice.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);
}
