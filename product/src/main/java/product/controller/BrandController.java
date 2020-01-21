package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import product.domain.Brand;
import product.service.BrandService;

import java.util.Optional;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{code}", method= RequestMethod.GET)
    public ResponseEntity findByCode(@PathVariable String code) {
        final Optional<Brand> brandOptional = brandService.findByCode(code);
        return brandOptional.map(brand -> new ResponseEntity<>(brand, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity findAll(){
        return new ResponseEntity<>(brandService.findAll(), HttpStatus.OK);
    }

}
