package br.com.scm.productservice.service;

import br.com.scm.productservice.model.ProductRequest;

public interface ProductService {
    long addProduct(ProductRequest productRequest);
}
