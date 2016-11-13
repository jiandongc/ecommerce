package product.mapper;

import org.springframework.stereotype.Component;
import product.data.BrandData;
import product.domain.Brand;

/**
 * Created by jiandong on 13/11/16.
 */

@Component
public class BrandDataMapper {

    public BrandData getValue(Brand brand){
        return new BrandData(brand.getId(), brand.getName());
    }
}
