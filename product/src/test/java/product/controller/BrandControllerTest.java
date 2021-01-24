package product.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.domain.Brand;

import java.time.LocalDate;

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

    @Test
    public void shouldFindAllValidBrands(){
        // Given
        brandRepository.save(Brand.builder()
                .name("NIKE")
                .code("nike")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .build());

        brandRepository.save(Brand.builder()
                .name("ADIDAS")
                .code("adidas")
                .startDate(LocalDate.now().plusDays(10))
                .endDate(null)
                .build());

        brandRepository.save(Brand.builder()
                .name("PUMA")
                .code("puma")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(2))
                .build());

        brandRepository.save(Brand.builder()
                .name("WILKO")
                .code("wilko")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .build());

        brandRepository.save(Brand.builder()
                .name("TESCO")
                .code("tesco")
                .startDate(LocalDate.now())
                .endDate(null)
                .build());

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<Brand[]> responseEntity = rest.exchange(BASE_URL, GET, httpEntity, Brand[].class);

        // Then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().length, is(3));
        assertThat(responseEntity.getBody()[0].getName(), is("NIKE"));
        assertThat(responseEntity.getBody()[1].getName(), is("WILKO"));
        assertThat(responseEntity.getBody()[2].getName(), is("TESCO"));
    }

    @Test
    public void shouldReturnBrandsInOrder(){
        // Given
        brandRepository.save(Brand.builder()
                .name("NIKE")
                .code("nike")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .ordering(3)
                .build());

        brandRepository.save(Brand.builder()
                .name("ADIDAS")
                .code("adidas")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .ordering(2)
                .build());

        brandRepository.save(Brand.builder()
                .name("TESCO")
                .code("tesco")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .ordering(null)
                .build());

        brandRepository.save(Brand.builder()
                .name("WILKO")
                .code("wilko")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(null)
                .ordering(1)
                .build());


        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Brand[]> responseEntity = rest.exchange(BASE_URL, GET, httpEntity, Brand[].class);

        // Then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().length, is(4));
        assertThat(responseEntity.getBody()[0].getName(), is("WILKO"));
        assertThat(responseEntity.getBody()[1].getName(), is("ADIDAS"));
        assertThat(responseEntity.getBody()[2].getName(), is("NIKE"));
        assertThat(responseEntity.getBody()[3].getName(), is("TESCO"));
    }


}