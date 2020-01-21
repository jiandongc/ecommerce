package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import product.service.ProductTagService;


@RestController
@RequestMapping("/tags")
public class ProductTagController {

    private final ProductTagService productTagService;

    @Autowired
    public ProductTagController(ProductTagService productTagService) {
        this.productTagService = productTagService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity findAll(){
        return new ResponseEntity<>(productTagService.findAll(), HttpStatus.OK);
    }

}
