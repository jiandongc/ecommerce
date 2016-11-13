package product.mapper;

import org.springframework.stereotype.Component;
import product.data.CategoryData;
import product.domain.Category;

/**
 * Created by jiandong on 13/11/16.
 */
@Component
public class CategoryDataMapper {

    public CategoryData getValue(Category category){
        return new CategoryData(category.getId(), category.getName());
    }
}
