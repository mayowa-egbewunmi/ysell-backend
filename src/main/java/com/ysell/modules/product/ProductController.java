package com.ysell.modules.product;

import com.ysell.common.constants.AppConstants;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.response.PageWrapper;
import com.ysell.modules.product.domain.ProductService;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author tchineke
 * @since 10 February, 2021
 */
@RestController
@RequestMapping(ProductController.PATH)
@RequiredArgsConstructor
public class ProductController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/products";

	private final ProductService productService;


    @GetMapping
    public PageWrapper<ProductResponse> getAllProductsPaged(@PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) Pageable page){
        return productService.getAllPaged(page);
    }


    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable("id") UUID productId){
        return productService.getById(productId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest request) {
        return productService.create(request);
    }


    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable UUID productId, @RequestBody @Valid ProductRequest request) {
        return productService.update(productId, request);
    }


    @GetMapping("/by-organisation")
    public List<ProductResponse> getProductsByOrganisation(@RequestParam("id") Set<UUID> organisationIds) {
        return productService.getProductsByOrganisationIds(organisationIds);
    }


    @DeleteMapping("/{id}")
    public ProductResponse deleteProduct(@PathVariable("id") UUID productId) {
        return productService.delete(productId);
    }


    @GetMapping("/categories")
    public List<ProductCategoryResponse> getProductCategories() {
        return productService.getProductCategories();
    }


    @PostMapping("/categories")
    public ProductCategoryResponse createOrUpdateProductCategory(@RequestBody @Valid ProductCategoryRequest request) {
        return productService.createOrUpdateProductCategory(request);
    }
}
