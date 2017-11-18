package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.data.Facet;
import product.data.FacetValue;
import product.data.ProductSearchData;
import product.domain.Attribute;
import product.domain.Category;
import product.domain.Key;
import product.domain.Product;
import product.mapper.FilterMapMapper;
import product.mapper.ProductSimpleDataMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final FilterMapMapper filterMapMapper;
    private final ProductSimpleDataMapper productMapper;

    @Autowired
    public ProductSearchServiceImpl(FilterMapMapper filterMapMapper,
                                    ProductSimpleDataMapper productMapper) {
        this.filterMapMapper = filterMapMapper;
        this.productMapper = productMapper;
    }

    @Override
    public ProductSearchData filter(Category category, List<Product> allProducts, String filterJsonStr) {
        final Map<String, List<String>> filterMap = filterMapMapper.getValue(filterJsonStr);
        final List<Product> products = this.filterProducts(allProducts, filterMap);

        final ProductSearchData.ProductSearchDataBuilder productSearchDataBuilder = ProductSearchData.builder();
        for(Product product : products){
            productSearchDataBuilder.addProduct(productMapper.getValueWithMainImage(product));
        }

        // build facets
        for(Key key : category.getFilterKeys()){
            final Facet facet = this.buildFacet(key, allProducts, filterMap);
            productSearchDataBuilder.addFacet(facet);
        }

        return productSearchDataBuilder.build();
    }

    private List<Product> filterProducts(List<Product> allProducts, Map<String, List<String>> filterMap){
        return allProducts.stream().filter(product -> {
            for (Object o : filterMap.entrySet()) {
                final Map.Entry pair = (Map.Entry) o;
                final String fieldName = (String) pair.getKey();
                final List fieldValues = (List) pair.getValue();
                boolean noneMatch = product.getAttributes().stream().noneMatch(attribute ->
                        attribute.getKeyName().equalsIgnoreCase(fieldName) && fieldValues.contains(attribute.getValue().toLowerCase()));

                if (noneMatch) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    private Facet buildFacet(Key key, List<Product> allProducts, Map<String, List<String>> filterMap) {
        final Facet.FacetBuilder facetBuilder = Facet.builder();
        facetBuilder.name(key.getName().toLowerCase());
        facetBuilder.hasSelectedValue(filterMap.containsKey(key.getName().toLowerCase()));

        final Map<String, List<String>> facetFilterMap = this.facetFilterMap(key, filterMap);
        final List<Product> products = this.filterProducts(allProducts, facetFilterMap);

        final Map<String, Integer> facetValueMap = new HashMap<>();
        for (Product product : products) {
            for (Attribute attribute : product.getAttributes()) {
                if (attribute.getKey().equals(key)) {
                    final String value = attribute.getValue().toLowerCase();
                    if (!facetValueMap.containsKey(value)) {
                        facetValueMap.put(value, 1);
                    } else {
                        int count = facetValueMap.get(value);
                        facetValueMap.put(value, ++count);
                    }
                }
            }
        }

        facetValueMap.keySet().forEach(name -> {
            final Integer count = facetValueMap.get(name);
            final FacetValue facetValue = FacetValue.builder()
                    .name(name)
                    .count(count)
                    .isSelected(filterMap.containsKey(key.getName().toLowerCase()) && filterMap.get(key.getName().toLowerCase()).contains(name))
                    .build();
            facetBuilder.addFacetValue(facetValue);
        });

        return facetBuilder.build();
    }

    private Map<String, List<String>> facetFilterMap(Key key, Map<String, List<String>> filterMap){
        final Map<String, List<String>> facetFilterMap = new HashMap<>();
        for(Object o: filterMap.entrySet()) {
            final Map.Entry pair = (Map.Entry) o;
            final String fieldName = (String) pair.getKey();
            final List fieldValues = (List) pair.getValue();
            if(!fieldName.equalsIgnoreCase(key.getName())){
                facetFilterMap.put(fieldName, fieldValues);
            }
        }
        return facetFilterMap;
    }





}
