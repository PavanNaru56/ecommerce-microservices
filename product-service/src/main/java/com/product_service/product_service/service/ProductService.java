package com.product_service.product_service.service;

import com.product_service.product_service.dtos.ProductRequest;
import com.product_service.product_service.model.Product;
import com.product_service.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequest productRequest) {

        Product product = new Product();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);

        return product;

    };

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    };

    public Product updateProduct(Long Id, ProductRequest productRequest){

        Product product = productRepository.findById(Id).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);

        return product;

    };

    public String deleteProduct(Long Id){
        Product product = productRepository.findById(Id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        return "Product has been deleted";
    };
}
