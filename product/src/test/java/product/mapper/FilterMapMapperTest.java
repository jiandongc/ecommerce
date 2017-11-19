package product.mapper;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;


public class FilterMapMapperTest {

    private FilterMapMapper mapper = new FilterMapMapper();

    @Test
    public void shouldMapFilterJsonStringToFilterMap() throws IOException {
        // Given
        final String json = "{\n" +
                "    \"color\": [\"blue\", \"red\", \"black\", \"pink\"],\n" +
                "    \"brand\": [\"nike\", \"adidas\", \"new balance\"],\n" +
                "    \"shape\": [\"squere\", \"triangle\"]\n" +
                "  }";

        // When
        final Map<String, List<String>> filterMap = mapper.getValue(json);

        // Then
        assertThat(filterMap.size(), is(3));
        assertThat(filterMap.get("color"), hasItems("blue", "red", "black", "pink"));
        assertThat(filterMap.get("brand"), hasItems("nike", "adidas", "new balance"));
        assertThat(filterMap.get("shape"), hasItems("squere", "triangle"));
    }

    @Test
    public void shouldReturnEmptyMapIfJsonStringIsNull(){
        // When
        final Map<String, List<String>> filterMap = mapper.getValue(null);

        // Then
        assertThat(filterMap.size(), is(0));
    }

}