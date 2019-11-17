package review.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import review.domain.Feedback;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

public class ReviewControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8085/reviews";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void shouldCreateFeedback(){
        final String feedbackJson = "{\"feedback\": \"123456\"}";
        final HttpEntity<String> feedbackPayload = new HttpEntity<String>(feedbackJson, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, feedbackPayload, String.class);
        assertThat(response.getStatusCode(), is(OK));
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks.size(), is(1));
        assertThat(feedbacks.get(0).getFeedback(), is("123456"));
    }

    @Test
    public void shouldFindFeedbackById(){
        Feedback savedFeedbck = feedbackRepository.save(Feedback.builder().feedback("love your website").creationDate(LocalDate.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"feedback\":\"love your website\""), is(true));
    }



}