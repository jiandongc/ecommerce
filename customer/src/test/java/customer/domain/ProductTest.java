package customer.domain;

import org.hamcrest.Matchers;
import org.junit.Test;

import static customer.domain.Product.Type.FAVOURITE;
import static customer.domain.Product.Type.NOTIFY_IN_STOCK;
import static java.time.LocalDate.now;
import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void shouldReturnTrueIfProductIdAndTypeIsTheSame(){
        // Given
        Product product = Product.builder().productCode("11L").type(FAVOURITE).startDate(now()).build();

        // When
        assertThat(product.hasSameProductCodeAndType(Product.builder().productCode("11L").type(FAVOURITE).startDate(now()).build()), Matchers.is(true));
    }

    @Test
    public void shouldReturnFalseIfProductIdIsDifferent(){
        // Given
        Product product = Product.builder().productCode("11L").type(FAVOURITE).startDate(now()).build();

        // When
        assertThat(product.hasSameProductCodeAndType(Product.builder().productCode("12L").type(FAVOURITE).startDate(now()).build()), Matchers.is(false));
    }

    @Test
    public void shouldReturnFalseIfTypeIsDifferent(){
        // Given
        Product product = Product.builder().productCode("11L").type(FAVOURITE).startDate(now()).build();

        // When
        assertThat(product.hasSameProductCodeAndType(Product.builder().productCode("11L").type(NOTIFY_IN_STOCK).startDate(now()).build()), Matchers.is(false));
    }

}