package customer.service;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.Assert.*;

public class UrlGeneratorTest {

    private UrlGenerator urlGenerator = new UrlGenerator();

    @Before
    public void setup(){
        ReflectionTestUtils.setField(urlGenerator, "hostUrl", "http://localhost:8000");
    }

    @Test
    public void shouldGeneratePasswordResetUrl(){
        String url = urlGenerator.generatePasswordResetUrl("abc-def");
        assertThat(url, CoreMatchers.is("http://localhost:8000/customer/password?token=abc-def"));
    }

}