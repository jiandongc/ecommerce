package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.domain.Brand;
import product.repository.BrandRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Brand> findByCode(String code) {
        return brandRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        final List<Brand> brands = brandRepository.findAll();
        final LocalDate today = LocalDate.now();
        return brands.stream()
                .filter(b -> b.getStartDate() != null && !today.isBefore(b.getStartDate()))
                .filter(b -> b.getEndDate() == null || !today.isAfter(b.getEndDate()))
                .sorted(BrandPredicate.orderingComparator())
                .collect(Collectors.toList());
    }
}
