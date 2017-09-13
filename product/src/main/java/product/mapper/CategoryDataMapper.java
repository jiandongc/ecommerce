package product.mapper;

import org.springframework.stereotype.Component;
import product.data.CategoryData;
import product.domain.Category;
import product.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiandong on 13/11/16.
 */
@Component
public class CategoryDataMapper {

    public CategoryData getValue(Category category,
                                 List<Category> parentsCategories,
                                 Map<Category, Integer> subCategories,
                                 List<Product> products){


        final List<CategoryData> parents = parentsCategories
                .stream().map(c -> CategoryData.builder()
                        .code(c.getCode())
                        .name(c.getName())
                        .build())
                .collect(Collectors.toList());

        final List<CategoryData> children = subCategories.keySet()
                .stream().map(c -> CategoryData.builder()
                        .code(c.getCode())
                        .name(c.getName())
                        .productTotal(subCategories.get(c))
                        .build())
                .collect(Collectors.toList());

        return CategoryData.builder()
                .code(category.getCode())
                .name(category.getName())
                .productTotal(products.size())
                .parents(parents)
                .children(children)
                .build();

    }
}
