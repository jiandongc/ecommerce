package product.service;

import product.domain.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    Optional<Brand> findByCode(String code);

    List<Brand> findAll();
}
