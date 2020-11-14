package com.ysell.modules.product;

import com.ysell.common.annotations.WrapResponse;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.product.domain.abstractions.ProductService;
import com.ysell.modules.product.models.request.CreateProductRequest;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductsByOrganisationRequest;
import com.ysell.modules.product.models.request.UpdateProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/products")
@RequiredArgsConstructor
@WrapResponse
public class ProductController {

	private final ProductService productService;

    @GetMapping("")
    @ResponseBody
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProductResponse getProduct(@PathVariable("id") long id){
        return productService.getProduct(id);
    }
    
    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }
    
    @PutMapping("")
    @ResponseBody
    public ProductResponse updateProduct(@RequestBody UpdateProductRequest request) {
        return productService.updateProduct(request);
    }
    
    @GetMapping("/by_organisation")
    @ResponseBody
    public List<ProductResponse> getProductsByOrganisation(@RequestParam("id") ArrayList<Long> ids) {
    	List<LookupDto> organisationLookups = ids.stream()
    			.map(id -> LookupDto.create(id))
    			.collect(Collectors.toList());
    	ProductsByOrganisationRequest request = new ProductsByOrganisationRequest(new HashSet<>(organisationLookups));
    	
        return productService.getProductsByOrganisations(request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ProductResponse deleteProduct(@PathVariable("id") long id) {
        return productService.deleteProduct(id);
    }
    
    @GetMapping("/categories")
    @ResponseBody
    public List<ProductCategoryResponse> getProductCategories() {
        return productService.getProductCategories();
    }
    
    @PostMapping("/categories")
    @ResponseBody
    public ProductCategoryResponse createOrUpdateProductCategory(@RequestBody ProductCategoryRequest request) {
        return productService.createOrUpdateProductCategory(request);
    }
}
