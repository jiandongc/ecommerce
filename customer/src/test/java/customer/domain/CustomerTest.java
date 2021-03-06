package customer.domain;

import org.junit.Test;

import java.util.List;

import static customer.domain.Product.Type.FAVOURITE;
import static java.time.LocalDate.now;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomerTest {

    @Test
    public void shouldGetValidProducts(){
        // Given
        final Customer customer = new Customer();
        customer.addProduct(Product.builder().productCode("11L").type(FAVOURITE).startDate(now()).build());
        customer.addProduct(Product.builder().productCode("12L").type(FAVOURITE).startDate(now().minusDays(1)).build());
        customer.addProduct(Product.builder().productCode("13L").type(FAVOURITE).startDate(now().plusDays(1)).build());
        customer.addProduct(Product.builder().productCode("14L").type(FAVOURITE).startDate(now().minusDays(5)).endDate(now().minusDays(1)).build());

        // When
        List<Product> validProducts = customer.getValidProducts();

        // Then
        assertThat(validProducts.size(), is(2));
        assertThat(validProducts.get(0).getProductCode(), is("11L"));
        assertThat(validProducts.get(1).getProductCode(), is("12L"));
    }

}