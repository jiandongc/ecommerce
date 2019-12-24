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
        ReflectionTestUtils.setField(urlGenerator, "webBaseUrl", "http://localhost:8000/app");
    }

    @Test
    public void shouldGeneratePasswordResetUrl(){
        String url = urlGenerator.generatePasswordResetUrl("d1320cf9-03d2-4bcc-ac93-71346c0c720d");
        assertThat(url, CoreMatchers.is("http://localhost:8000/app/#!/login/password?token=d1320cf9-03d2-4bcc-ac93-71346c0c720d"));
    }

}