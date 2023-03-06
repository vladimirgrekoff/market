package com.grekoff.market.core.controllers;

import com.grekoff.market.api.ProductDto;
import com.grekoff.market.core.converters.ProductConverter;
import com.grekoff.market.core.exceptions.ResourceNotFoundException;
import com.grekoff.market.core.services.ProductsService;
import com.grekoff.market.core.validators.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//import org.hibernate.mapping.List;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class ProductsController {
    private final ProductsService productsService;
    private final ProductConverter productConverter;
    private final ProductValidator productValidator;

    // http://localhost:8189/market-core/api/v1/products

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productsService.findAll();
    }

    @GetMapping("/pages")
    public Page<ProductDto> getAllPagesProducts(
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "part_title", required = false) String partTitle,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", defaultValue = "5") Integer limit,
            @RequestParam(name = "first", defaultValue = "false") Boolean first,
            @RequestParam(name = "last", defaultValue = "false") Boolean last

    ) {
        return productsService.findAllPages(minPrice, maxPrice, partTitle, offset, limit, first, last).map(p -> productConverter.entityToDto(p));

    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productConverter.entityToDto(productsService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + id + " не найден")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewProducts(@RequestBody ProductDto productDto) {
        productsService.createNewProduct(productDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@RequestBody ProductDto productDto) {
        productValidator.validate(productDto);
        productsService.update(productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productsService.deleteById(id);
    }
}
