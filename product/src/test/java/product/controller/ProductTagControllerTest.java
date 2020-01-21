package product.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.domain.Category;
import product.domain.Product;
import product.domain.ProductTag;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;

public class ProductTagControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldFindAllDistinctTags(){
        // Given
        Category category = new Category();
        category.setHidden(false);
        category.setName("food");
        category.setDescription("delicious");
        category.setImageUrl("img/0001.jpg");
        category.setCode("FD");
        categoryRepository.save(category);

        final Product productOne = new Product();
        productOne.setCategory(category);
        productOne.setName("productOne");
        productOne.setDescription("productOne description");
        productOne.addTag(ProductTag.builder().tag("sale-expired").startDate(LocalDate.now().minusDays(10)).endDate(LocalDate.now().minusDays(3)).build());
        productOne.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now().minusDays(10)).endDate(null).build());
        productOne.addTag(ProductTag.builder().tag("sale-future").startDate(LocalDate.now().plusDays(10)).endDate(null).build());
        productRepository.save(productOne);

        final Product productTwo = new Product();
        productTwo.setCategory(category);
        productTwo.setName("productTwo");
        productTwo.setDescription("productTwo description");
        productTwo.addTag(ProductTag.builder().tag("popular-near-expire").startDate(LocalDate.now().minusDays(10)).endDate(LocalDate.now()).build());
        productTwo.addTag(ProductTag.builder().tag("popular").startDate(LocalDate.now()).endDate(null).build());
        productTwo.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now().minusDays(10)).endDate(null).build());
        productRepository.save(productTwo);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<ProductTag[]> responseEntity = restTemplate.exchange("/tags", GET, httpEntity, ProductTag[].class);

        // Then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().length, is(3));
        assertThat(responseEntity.getBody()[0].getTag(), is("sale"));
        assertThat(responseEntity.getBody()[1].getTag(), is("popular-near-expire"));
        assertThat(responseEntity.getBody()[2].getTag(), is("popular"));

    }

}