package product.service;

import product.domain.Brand;

import java.util.Comparator;

public class BrandPredicate {

    public static Comparator<Brand> orderingComparator() {
        return (Brand brand1, Brand brand2) -> {
            if (brand1.getOrdering() == null && brand2.getOrdering() == null) {
                return 0;
            }
            if (brand1.getOrdering() == null) return 1;
            if (brand2.getOrdering() == null) return -1;
            return brand1.getOrdering().compareTo(brand2.getOrdering());
        };
    }
}
