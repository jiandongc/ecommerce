package product.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.data.CategoryData;
import product.domain.*;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;


/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryControllerTest extends AbstractControllerTest {

    private final String BASE_URL = "http://localhost:8083/categories/";
    private final TestRestTemplate rest = new TestRestTemplate();

    /*
                            c1
                            c2
                            c3
                 c4 		p1		   c5
                 p2			        p3    p4
    */
    @Test
    public void shouldReturnCategoryDataByCode(){
        // Given
        final Category c1 = new Category();
        c1.setCode("c1");
        c1.setName("c1");
        categoryRepository.save(c1);

        final Category c2 = new Category();
        c2.setCode("c2");
        c2.setName("c2");
        c2.setParent(c1);
        categoryRepository.save(c2);

        final Category c3 = new Category();
        c3.setCode("c3");
        c3.setName("c3");
        c3.setParent(c2);
        categoryRepository.save(c3);

        final Category c4 = new Category();
        c4.setCode("c4");
        c4.setName("c4");
        c4.setParent(c3);
        categoryRepository.save(c4);

        final Category c5 = new Category();
        c5.setCode("c5");
        c5.setName("c5");
        c5.setParent(c3);
        categoryRepository.save(c5);

        final Product p1 = new Product();
        p1.setCode("p1");
        p1.setName("p1");
        p1.setCategory(c3);
        p1.setStartDate(LocalDate.now());
        productRepository.save(p1);

        final Product p2 = new Product();
        p2.setCode("p2");
        p2.setName("p2");
        p2.setCategory(c4);
        p2.setStartDate(LocalDate.now());
        productRepository.save(p2);

        final Product p3 = new Product();
        p3.setCode("p3");
        p3.setName("p3");
        p3.setCategory(c5);
        p3.setStartDate(LocalDate.now());
        productRepository.save(p3);

        final Product p4 = new Product();
        p4.setCode("p4");
        p4.setName("p4");
        p4.setCategory(c5);
        p4.setStartDate(LocalDate.now());
        productRepository.save(p4);

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CategoryData> c1Response = rest.exchange(BASE_URL + "c1", GET, httpEntity, CategoryData.class);
        assertThat(c1Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c1Response.getBody().getName(), is("c1"));
        assertThat(c1Response.getBody().getCode(), is("c1"));
        assertThat(c1Response.getBody().getProductTotal(), is(4));
        assertThat(c1Response.getBody().getParents().size(), is(1));
        assertThat(c1Response.getBody().getParents().get(0).getName(), is("c1"));
        assertThat(c1Response.getBody().getParents().get(0).getCode(), is("c1"));
        assertThat(c1Response.getBody().getChildren().size(), is(1));
        assertThat(c1Response.getBody().getChildren().get(0).getName(), is("c2"));
        assertThat(c1Response.getBody().getChildren().get(0).getCode(), is("c2"));
        assertThat(c1Response.getBody().getChildren().get(0).getProductTotal(), is(4));

        final ResponseEntity<CategoryData> c2Response = rest.exchange(BASE_URL + "c2", GET, httpEntity, CategoryData.class);
        assertThat(c2Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c2Response.getBody().getName(), is("c2"));
        assertThat(c2Response.getBody().getCode(), is("c2"));
        assertThat(c2Response.getBody().getProductTotal(), is(4));
        assertThat(c2Response.getBody().getParents().size(), is(2));
        assertThat(c2Response.getBody().getParents().get(0).getName(), is("c1"));
        assertThat(c2Response.getBody().getParents().get(0).getCode(), is("c1"));
        assertThat(c2Response.getBody().getParents().get(1).getName(), is("c2"));
        assertThat(c2Response.getBody().getParents().get(1).getCode(), is("c2"));
        assertThat(c2Response.getBody().getChildren().size(), is(1));
        assertThat(c2Response.getBody().getChildren().get(0).getName(), is("c3"));
        assertThat(c2Response.getBody().getChildren().get(0).getCode(), is("c3"));
        assertThat(c2Response.getBody().getChildren().get(0).getProductTotal(), is(4));

        final ResponseEntity<CategoryData> c3Response = rest.exchange(BASE_URL + "c3", GET, httpEntity, CategoryData.class);
        assertThat(c3Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c3Response.getBody().getName(), is("c3"));
        assertThat(c3Response.getBody().getCode(), is("c3"));
        assertThat(c3Response.getBody().getProductTotal(), is(4));
        assertThat(c3Response.getBody().getParents().size(), is(3));
        assertThat(c3Response.getBody().getParents().get(0).getName(), is("c1"));
        assertThat(c3Response.getBody().getParents().get(0).getCode(), is("c1"));
        assertThat(c3Response.getBody().getParents().get(1).getName(), is("c2"));
        assertThat(c3Response.getBody().getParents().get(1).getCode(), is("c2"));
        assertThat(c3Response.getBody().getParents().get(2).getName(), is("c3"));
        assertThat(c3Response.getBody().getParents().get(2).getCode(), is("c3"));
        assertThat(c3Response.getBody().getChildren().size(), is(2));
        assertThat(c3Response.getBody().getChildren().get(0).getName(), is("c4"));
        assertThat(c3Response.getBody().getChildren().get(0).getCode(), is("c4"));
        assertThat(c3Response.getBody().getChildren().get(0).getProductTotal(), is(1));
        assertThat(c3Response.getBody().getChildren().get(1).getName(), is("c5"));
        assertThat(c3Response.getBody().getChildren().get(1).getCode(), is("c5"));
        assertThat(c3Response.getBody().getChildren().get(1).getProductTotal(), is(2));

        final ResponseEntity<CategoryData> c4Response = rest.exchange(BASE_URL + "c4", GET, httpEntity, CategoryData.class);
        assertThat(c4Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c4Response.getBody().getName(), is("c4"));
        assertThat(c4Response.getBody().getCode(), is("c4"));
        assertThat(c4Response.getBody().getProductTotal(), is(1));
        assertThat(c4Response.getBody().getParents().size(), is(4));
        assertThat(c4Response.getBody().getParents().get(0).getName(), is("c1"));
        assertThat(c4Response.getBody().getParents().get(0).getCode(), is("c1"));
        assertThat(c4Response.getBody().getParents().get(1).getName(), is("c2"));
        assertThat(c4Response.getBody().getParents().get(1).getCode(), is("c2"));
        assertThat(c4Response.getBody().getParents().get(2).getName(), is("c3"));
        assertThat(c4Response.getBody().getParents().get(2).getCode(), is("c3"));
        assertThat(c4Response.getBody().getParents().get(3).getName(), is("c4"));
        assertThat(c4Response.getBody().getParents().get(3).getCode(), is("c4"));
        assertThat(c4Response.getBody().getChildren().size(), is(0));

        final ResponseEntity<CategoryData> c5Response = rest.exchange(BASE_URL + "c5", GET, httpEntity, CategoryData.class);
        assertThat(c5Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c5Response.getBody().getName(), is("c5"));
        assertThat(c5Response.getBody().getCode(), is("c5"));
        assertThat(c5Response.getBody().getProductTotal(), is(2));
        assertThat(c5Response.getBody().getParents().size(), is(4));
        assertThat(c5Response.getBody().getParents().get(0).getName(), is("c1"));
        assertThat(c5Response.getBody().getParents().get(0).getCode(), is("c1"));
        assertThat(c5Response.getBody().getParents().get(1).getName(), is("c2"));
        assertThat(c5Response.getBody().getParents().get(1).getCode(), is("c2"));
        assertThat(c5Response.getBody().getParents().get(2).getName(), is("c3"));
        assertThat(c5Response.getBody().getParents().get(2).getCode(), is("c3"));
        assertThat(c5Response.getBody().getParents().get(3).getName(), is("c5"));
        assertThat(c5Response.getBody().getParents().get(3).getCode(), is("c5"));
        assertThat(c5Response.getBody().getChildren().size(), is(0));
    }

    /*
                        c1
                        c2
                        c3
             c4 				   c5
    */
    @Test
    public void shouldReturnSubCategoriesByLevel(){
        // Given
        final Category c1 = new Category();
        c1.setCode("c1");
        c1.setName("c1");
        categoryRepository.save(c1);

        final Category c2 = new Category();
        c2.setCode("c2");
        c2.setName("c2");
        c2.setParent(c1);
        categoryRepository.save(c2);

        final Category c3 = new Category();
        c3.setCode("c3");
        c3.setName("c3");
        c3.setParent(c2);
        categoryRepository.save(c3);

        final Category c4 = new Category();
        c4.setCode("c4");
        c4.setName("c4");
        c4.setParent(c3);
        categoryRepository.save(c4);

        final Category c5 = new Category();
        c5.setCode("c5");
        c5.setName("c5");
        c5.setParent(c3);
        categoryRepository.save(c5);

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CategoryData> c1Response = rest.exchange(BASE_URL + "c1?level=2", GET, httpEntity, CategoryData.class);
        assertThat(c1Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c1Response.getBody().getName(), is("c1"));
        assertThat(c1Response.getBody().getCode(), is("c1"));
        assertThat(c1Response.getBody().getChildren().size(), is(1));
        CategoryData subCategory = c1Response.getBody().getChildren().get(0);
        assertThat(subCategory.getName(), is("c2"));
        assertThat(subCategory.getCode(), is("c2"));
        assertThat(subCategory.getChildren().size(), is(1));
        CategoryData subSubCategory = subCategory.getChildren().get(0);
        assertThat(subSubCategory.getName(), is("c3"));
        assertThat(subSubCategory.getCode(), is("c3"));
        assertThat(subSubCategory.getChildren(), is(nullValue()));

        final ResponseEntity<CategoryData> c2Response = rest.exchange(BASE_URL + "c2?level=2", GET, httpEntity, CategoryData.class);
        assertThat(c2Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c2Response.getBody().getName(), is("c2"));
        assertThat(c2Response.getBody().getCode(), is("c2"));
        assertThat(c2Response.getBody().getChildren().size(), is(1));
        subCategory = c2Response.getBody().getChildren().get(0);
        assertThat(subCategory.getName(), is("c3"));
        assertThat(subCategory.getCode(), is("c3"));
        assertThat(subCategory.getChildren().size(), is(2));
        subSubCategory = subCategory.getChildren().get(0);
        assertThat(subSubCategory.getName(), is("c4"));
        assertThat(subSubCategory.getCode(), is("c4"));
        assertThat(subSubCategory.getChildren(), is(nullValue()));
        subSubCategory = subCategory.getChildren().get(1);
        assertThat(subSubCategory.getName(), is("c5"));
        assertThat(subSubCategory.getCode(), is("c5"));
        assertThat(subSubCategory.getChildren(), is(nullValue()));

        final ResponseEntity<CategoryData> c3Response = rest.exchange(BASE_URL + "c3?level=2", GET, httpEntity, CategoryData.class);
        assertThat(c3Response.getStatusCode(), is(HttpStatus.OK));
        assertThat(c3Response.getBody().getName(), is("c3"));
        assertThat(c3Response.getBody().getCode(), is("c3"));
        assertThat(c3Response.getBody().getChildren().size(), is(2));
        subCategory = c3Response.getBody().getChildren().get(0);
        assertThat(subCategory.getName(), is("c4"));
        assertThat(subCategory.getCode(), is("c4"));
        assertThat(subCategory.getChildren(), is(nullValue()));
        subCategory = c3Response.getBody().getChildren().get(1);
        assertThat(subCategory.getName(), is("c5"));
        assertThat(subCategory.getCode(), is("c5"));
        assertThat(subCategory.getChildren(), is(nullValue()));
    }
}
