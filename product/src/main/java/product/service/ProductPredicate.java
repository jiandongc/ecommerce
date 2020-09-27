package product.service;

import product.domain.Product;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ProductPredicate {

    public static Predicate<Product> brandFilter(String brandCode) {
        return product -> product.getBrand() != null && brandCode.equals(product.getBrand().getCode());
    }

    public static Predicate<Product> tagsFilter(List<String> tags) {
        return product -> product.hasTag(tags);
    }

    public static Comparator<Product> priceAscComparator(){
        return Comparator.comparing(Product::getCurrentPrice);
    }

    public static Comparator<Product> priceDescComparator(){
        return Comparator.comparing(Product::getCurrentPrice).reversed();
    }

    public static Comparator<Product> orderingComparator(){
        return Comparator.comparing(Product::getOrdering);
    }

}
