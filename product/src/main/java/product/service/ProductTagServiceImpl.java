package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.domain.ProductTag;
import product.repository.ProductTagRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTagServiceImpl implements ProductTagService {

    private final ProductTagRepository productTagRepository;

    @Autowired
    public ProductTagServiceImpl(ProductTagRepository productTagRepository) {
        this.productTagRepository = productTagRepository;
    }

    @Override
    public List<ProductTag> findAll() {
        final List<ProductTag> tags = productTagRepository.findAll();
        final LocalDate today = LocalDate.now();
        return tags.stream()
                .filter(t -> t.getStartDate() != null && !today.isBefore(t.getStartDate()))
                .filter(t -> t.getEndDate() == null || !today.isAfter(t.getEndDate()))
                .distinct()
                .collect(Collectors.toList());
    }
}


