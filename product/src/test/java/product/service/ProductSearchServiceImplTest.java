package product.service;

import org.junit.Before;
import org.junit.Test;
import product.data.Facet;
import product.data.FacetValue;
import product.data.ProductSearchData;
import product.data.ProductSimpleData;
import product.domain.Attribute;
import product.domain.Category;
import product.domain.Key;
import product.domain.Product;
import product.mapper.FilterMapMapper;
import product.mapper.ProductSimpleDataMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ProductSearchServiceImplTest {

    private FilterMapMapper filterMapMapper;
    private ProductSimpleDataMapper productMapper;
    private ProductSearchService searchService;

    @Before
    public void setup(){
        this.filterMapMapper = new FilterMapMapper();
        this.productMapper = new ProductSimpleDataMapper();
        this.searchService = new ProductSearchServiceImpl(filterMapMapper, productMapper);
    }

    @Test
    public void shouldFilterProductBasedOnJsonFilterString(){
        // Given
        final Key color = new Key();
        color.setName("color");
        final Key brand = new Key();
        brand.setName("brand");
        final Attribute nike = new Attribute();
        nike.setKey(brand);
        nike.setValue("nike");
        final Attribute adidas = new Attribute();
        adidas.setKey(brand);
        adidas.setValue("adidas");
        final Attribute puma = new Attribute();
        puma.setKey(brand);
        puma.setValue("puma");
        final Attribute blue = new Attribute();
        blue.setKey(color);
        blue.setValue("blue");
        final Attribute red = new Attribute();
        red.setKey(color);
        red.setValue("red");
        final Attribute yellow = new Attribute();
        yellow.setKey(color);
        yellow.setValue("yellow");

        final Product redNike = new Product();
        redNike.setName("redNike");
        redNike.setAttributes(asList(red, nike));
        this.setOtherProductProperties(redNike);

        final Product redAdidas = new Product();
        redAdidas.setName("redAdidas");
        redAdidas.setAttributes(asList(red, adidas));
        this.setOtherProductProperties(redAdidas);

        final Product blueNike = new Product();
        blueNike.setName("blueNike");
        blueNike.setAttributes(asList(blue, nike));
        this.setOtherProductProperties(blueNike);

        final Product blueAdidas = new Product();
        blueAdidas.setName("blueAdidas");
        blueAdidas.setAttributes(asList(blue, adidas));
        this.setOtherProductProperties(blueAdidas);

        final Product yellowAdidas = new Product();
        yellowAdidas.setName("yellowAdidas");
        yellowAdidas.setAttributes(asList(yellow, adidas));
        this.setOtherProductProperties(yellowAdidas);

        final Product redPuma = new Product();
        redPuma.setName("redPuma");
        redPuma.setAttributes(asList(red, puma));
        this.setOtherProductProperties(redPuma);

        final Category category = new Category();
        category.setFilterKeys(Arrays.asList(color, brand));

        // When
        String json = "{\n" +
                "    \"color\": [\"blue\", \"red\"],\n" +
                "    \"brand\": [\"nike\", \"adidas\"]\n" +
                "  }";
        ProductSearchData productSearchData
                = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json);
        // Then
        List<ProductSimpleData> products = productSearchData.getProducts();
        assertThat(products.size(), is(4));
        assertThat(products.get(0).getName(), is("redNike"));
        assertThat(products.get(1).getName(), is("redAdidas"));
        assertThat(products.get(2).getName(), is("blueNike"));
        assertThat(products.get(3).getName(), is("blueAdidas"));

        List<Facet> facets = productSearchData.getFacets();
        assertThat(facets.size(), is(2));
        Facet colorFacet = Facet.builder().name("color").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("red").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("blue").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("yellow").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(0), is(colorFacet));
        Facet brandFacet = Facet.builder().name("brand").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("nike").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("adidas").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("puma").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(1), is(brandFacet));

        // When
        json = "{\n" +
                "    \"color\": [\"blue\"]\n" +
                "  }";
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json);
        // Then
        products = productSearchData.getProducts();
        assertThat(products.size(), is(2));
        assertThat(products.get(0).getName(), is("blueNike"));
        assertThat(products.get(1).getName(), is("blueAdidas"));

        facets = productSearchData.getFacets();
        assertThat(facets.size(), is(2));
        colorFacet = Facet.builder().name("color").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("red").count(3).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("blue").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("yellow").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(0), is(colorFacet));
        brandFacet = Facet.builder().name("brand").hasSelectedValue(false)
                .addFacetValue(FacetValue.builder().name("nike").count(1).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("adidas").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(1), is(brandFacet));

        // When
        json = "{\n" +
                "    \"brand\": [\"nike\"]\n" +
                "  }";
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json);
        // Then
        products = productSearchData.getProducts();
        assertThat(products.size(), is(2));
        assertThat(products.get(0).getName(), is("redNike"));
        assertThat(products.get(1).getName(), is("blueNike"));

        facets = productSearchData.getFacets();
        assertThat(facets.size(), is(2));
        colorFacet = Facet.builder().name("color").hasSelectedValue(false)
                .addFacetValue(FacetValue.builder().name("red").count(1).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("blue").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(0), is(colorFacet));
        brandFacet = Facet.builder().name("brand").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("nike").count(2).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("adidas").count(3).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("puma").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(1), is(brandFacet));

        // When
        json = "{\n" +
                "    \"color\": [\"red\"],\n" +
                "    \"brand\": [\"nike\"]\n" +
                "  }";
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json);
        // Then
        products = productSearchData.getProducts();
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getName(), is("redNike"));

        facets = productSearchData.getFacets();
        assertThat(facets.size(), is(2));
        colorFacet = Facet.builder().name("color").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("red").count(1).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("blue").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(0), is(colorFacet));
        brandFacet = Facet.builder().name("brand").hasSelectedValue(true)
                .addFacetValue(FacetValue.builder().name("nike").count(1).isSelected(true).build())
                .addFacetValue(FacetValue.builder().name("adidas").count(1).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("puma").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(1), is(brandFacet));

        // When
        json = "{}";
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json);
        // Then
        products = productSearchData.getProducts();
        assertThat(products.size(), is(6));
        assertThat(products.get(0).getName(), is("redNike"));
        assertThat(products.get(1).getName(), is("redAdidas"));
        assertThat(products.get(2).getName(), is("redPuma"));
        assertThat(products.get(3).getName(), is("blueNike"));
        assertThat(products.get(4).getName(), is("blueAdidas"));
        assertThat(products.get(5).getName(), is("yellowAdidas"));
        facets = productSearchData.getFacets();
        assertThat(facets.size(), is(2));
        colorFacet = Facet.builder().name("color").hasSelectedValue(false)
                .addFacetValue(FacetValue.builder().name("red").count(3).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("blue").count(2).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("yellow").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(0), is(colorFacet));
        brandFacet = Facet.builder().name("brand").hasSelectedValue(false)
                .addFacetValue(FacetValue.builder().name("nike").count(2).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("adidas").count(3).isSelected(false).build())
                .addFacetValue(FacetValue.builder().name("puma").count(1).isSelected(false).build())
                .build();
        assertThat(facets.get(1), is(brandFacet));
    }

    private void setOtherProductProperties(Product product){
        product.setImages(new ArrayList<>());
        product.setSkus(new ArrayList<>());
    }
}