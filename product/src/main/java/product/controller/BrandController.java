package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.domain.Brand;
import product.service.BrandService;

import java.util.Collections;
import java.util.List;
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
    public ResponseEntity findAll(@RequestParam(value = "sort", required = false) String sort){
        List<Brand> brands = brandService.findAll();
        if (sort != null && sort.equalsIgnoreCase("random")) {
            Collections.shuffle(brands);
        }

        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

}
