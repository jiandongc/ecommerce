package product.service;

import product.domain.Brand;

import java.util.Optional;

public interface BrandService {

    Optional<Brand> findByCode(String code);
}
