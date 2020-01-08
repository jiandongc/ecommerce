package product.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.domain.Brand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;

public class BrandControllerTest extends AbstractControllerTest {

    private final String BASE_URL = "http://localhost:8083/brands";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void shouldGetBrandByCode(){
        // Given
        brandRepository.save(Brand.builder()
                .name("NIKE")
                .code("nike")
                .country("USA")
                .description("sports brand")
                .imageUrl("http://abc.jpeg").build());

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<Brand> responseEntity = rest.exchange(BASE_URL + "/nike", GET, httpEntity, Brand.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getName(), is("NIKE"));
        assertThat(responseEntity.getBody().getCode(), is("nike"));
        assertThat(responseEntity.getBody().getCountry(), is("USA"));
        assertThat(responseEntity.getBody().getDescription(), is("sports brand"));
        assertThat(responseEntity.getBody().getImageUrl(), is("http://abc.jpeg"));
    }

    @Test
    public void shouldReturn404IfBrandCodeDoesNotExist(){
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<Brand> response = rest.exchange(BASE_URL + "/unknown", GET, httpEntity, Brand.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }


}