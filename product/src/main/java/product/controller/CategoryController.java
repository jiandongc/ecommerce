package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.data.CategoryData;
import product.service.CategoryService;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER', 'ROLE_ANONYMOUS')")
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity findCategoryByCode(@PathVariable String code, @RequestParam(value = "level", required = false) Integer level) {
        if (level == null) {
            Optional<CategoryData> categoryData = categoryService.getCategoryData(code);
            return categoryData.map(data -> new ResponseEntity<>(data, HttpStatus.OK)).orElse(new ResponseEntity<>(NOT_FOUND));
        } else {
            Optional<CategoryData> categoryData = categoryService.findSubCategories(code, level);
            return categoryData.map(data -> new ResponseEntity<>(data, HttpStatus.OK)).orElse(new ResponseEntity<>(NOT_FOUND));
        }
    }
}
