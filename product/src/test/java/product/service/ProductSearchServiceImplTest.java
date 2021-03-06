package product.service;

import org.junit.Before;
import org.junit.Test;
import product.data.Facet;
import product.data.FacetValue;
import product.data.ProductSearchData;
import product.data.ProductSimpleData;
import product.domain.*;
import product.mapper.FilterMapMapper;
import product.mapper.ProductSimpleDataMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        final Product redNike = new Product();
        redNike.setName("redNike");
        redNike.addAttribute(ProductAttribute.builder().key("color").value("red").build());
        redNike.addAttribute(ProductAttribute.builder().key("brand").value("nike").build());
        redNike.setOrdering(1);
        this.setOtherProductProperties(redNike);

        final Product redAdidas = new Product();
        redAdidas.setName("redAdidas");
        redAdidas.addAttribute(ProductAttribute.builder().key("color").value("red").build());
        redAdidas.addAttribute(ProductAttribute.builder().key("brand").value("adidas").build());
        redAdidas.setOrdering(1);
        this.setOtherProductProperties(redAdidas);

        final Product blueNike = new Product();
        blueNike.setName("blueNike");
        blueNike.addAttribute(ProductAttribute.builder().key("color").value("blue").build());
        blueNike.addAttribute(ProductAttribute.builder().key("brand").value("nike").build());
        blueNike.setOrdering(1);
        this.setOtherProductProperties(blueNike);

        final Product blueAdidas = new Product();
        blueAdidas.setName("blueAdidas");
        blueAdidas.addAttribute(ProductAttribute.builder().key("color").value("blue").build());
        blueAdidas.addAttribute(ProductAttribute.builder().key("brand").value("adidas").build());
        blueAdidas.setOrdering(1);
        this.setOtherProductProperties(blueAdidas);

        final Product yellowAdidas = new Product();
        yellowAdidas.setName("yellowAdidas");
        yellowAdidas.addAttribute(ProductAttribute.builder().key("color").value("yellow").build());
        yellowAdidas.addAttribute(ProductAttribute.builder().key("brand").value("adidas").build());
        yellowAdidas.setOrdering(1);
        this.setOtherProductProperties(yellowAdidas);

        final Product redPuma = new Product();
        redPuma.setName("redPuma");
        redPuma.addAttribute(ProductAttribute.builder().key("color").value("red").build());
        redPuma.addAttribute(ProductAttribute.builder().key("brand").value("puma").build());
        redPuma.setOrdering(1);
        this.setOtherProductProperties(redPuma);

        final Category category = new Category();
        category.addCategoryAttribute(CategoryAttribute.builder().key("color").ordering(0).build());
        category.addCategoryAttribute(CategoryAttribute.builder().key("brand").ordering(1).build());

        // When
        String json = "{\n" +
                "    \"color\": [\"blue\", \"red\"],\n" +
                "    \"brand\": [\"nike\", \"adidas\"]\n" +
                "  }";
        ProductSearchData productSearchData
                = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json, null);
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
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json, null);
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
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json, null);
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
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json, null);
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
        productSearchData = searchService.filter(category, asList(redNike, redAdidas, redPuma, blueNike, blueAdidas, yellowAdidas), json, null);
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

    @Test
    public void shouldSortProductsByPrice(){
        // Given
        final Category category = mock(Category.class);
        final Product p1 = mock(Product.class);
        when(p1.getCurrentPrice()).thenReturn(ONE);
        when(p1.getName()).thenReturn("p1");
        final Product p2 = mock(Product.class);
        when(p2.getCurrentPrice()).thenReturn(TEN);
        when(p2.getName()).thenReturn("p2");
        final Product p3 = mock(Product.class);
        when(p3.getCurrentPrice()).thenReturn(new BigDecimal(20));
        when(p3.getName()).thenReturn("p3");

        // When & Then
        ProductSearchData productSearchData = searchService.filter(category, asList(p1, p2, p3), "{}", "pricedesc");
        List<ProductSimpleData> products = productSearchData.getProducts();
        assertThat(products.size(), is(3));
        assertThat(products.get(0).getName(), is("p3"));
        assertThat(products.get(1).getName(), is("p2"));
        assertThat(products.get(2).getName(), is("p1"));

        // When & Then
        productSearchData = searchService.filter(category, asList(p1, p2, p3), "{}", "priceasc");
        products = productSearchData.getProducts();
        assertThat(products.size(), is(3));
        assertThat(products.get(0).getName(), is("p1"));
        assertThat(products.get(1).getName(), is("p2"));
        assertThat(products.get(2).getName(), is("p3"));
    }

    @Test
    public void shouldSortProductsByOrderingNumber(){
        // Given
        final Category category = mock(Category.class);
        final Product p1 = mock(Product.class);
        when(p1.getOrdering()).thenReturn(3);
        when(p1.getName()).thenReturn("p1");
        final Product p2 = mock(Product.class);
        when(p2.getOrdering()).thenReturn(1);
        when(p2.getName()).thenReturn("p2");
        final Product p3 = mock(Product.class);
        when(p3.getOrdering()).thenReturn(2);
        when(p3.getName()).thenReturn("p3");

        // When & Then
        ProductSearchData productSearchData = searchService.filter(category, asList(p1, p2, p3), null, null);
        List<ProductSimpleData> products = productSearchData.getProducts();
        assertThat(products.size(), is(3));
        assertThat(products.get(0).getName(), is("p2"));
        assertThat(products.get(1).getName(), is("p3"));
        assertThat(products.get(2).getName(), is("p1"));
    }

    private void setOtherProductProperties(Product product){
        product.setImages(new ArrayList<>());
        product.setSkus(new ArrayList<>());
    }
}