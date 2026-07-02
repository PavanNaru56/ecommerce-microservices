package com.product_service.product_service.controller;

import com.product_service.product_service.dtos.ProductRequest;
import com.product_service.product_service.model.Product;
import com.product_service.product_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest){

        Product product = productService.createProduct(productRequest);

        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);

    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts(){

        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(products);

    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> getProductById(@PathVariable Long Id){
        Product product = productService.getProductById(Id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/update/{Id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest productRequest, @PathVariable Long Id){

        Product product = productService.updateProduct(Id, productRequest);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long Id){
        String res =  productService.deleteProduct(Id);
        return ResponseEntity.ok().body(res);
    }


}
