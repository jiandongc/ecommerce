package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import product.domain.ProductTag;
import product.service.ProductTagService;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/tags")
public class ProductTagController {

    private final ProductTagService productTagService;

    @Autowired
    public ProductTagController(ProductTagService productTagService) {
        this.productTagService = productTagService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER', 'ROLE_ANONYMOUS')")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity findAll(@RequestParam(value = "sort", required = false) String sort){
        List<ProductTag> tags = productTagService.findAll();
        if (sort != null && sort.equalsIgnoreCase("random")) {
            Collections.shuffle(tags);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

}
