package review.controller;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import review.domain.Answer;
import review.domain.Feedback;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;

public class ReviewControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8085/reviews";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void shouldCreateFeedback(){
        final String feedbackJson = "{\"text\": \"123456\"}";
        final HttpEntity<String> feedbackPayload = new HttpEntity<String>(feedbackJson, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, feedbackPayload, String.class);
        assertThat(response.getStatusCode(), is(OK));
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks.size(), is(1));
        assertThat(feedbacks.get(0).getText(), is("123456"));
    }

    @Test
    public void shouldFindFeedbackById(){
        Feedback savedFeedbck = feedbackRepository.save(Feedback.builder().text("love your website").creationDate(LocalDate.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"text\":\"love your website\""), is(true));
    }

    @Test
    public void shouldAddAnswerToFeedback(){
        Feedback savedFeedbck = feedbackRepository.save(Feedback.builder().text("love your website").creationDate(LocalDate.now()).build());
        final String answerJson = "{\"text\": \"thank you\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(answerJson, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id(), PUT, payload, String.class);
        assertThat(response.getStatusCode(), is(OK));
        Feedback updatedFeedback = feedbackRepository.findBy_id(savedFeedbck.get_id());
        assertThat(updatedFeedback.getText(), is("love your website"));
        assertThat(updatedFeedback.getAnswers().size(), is(1));
        assertThat(updatedFeedback.getAnswers().get(0).getText(), is("thank you"));
    }

    @Test
    public void shouldAddVoteToFeedback(){
        Feedback savedFeedbck = feedbackRepository.save(Feedback.builder().text("love your website").creationDate(LocalDate.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id() + "/vote", PUT, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        Feedback updatedFeedback = feedbackRepository.findBy_id(savedFeedbck.get_id());
        assertThat(updatedFeedback.getVote(), is(1));

        response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id() + "/vote", PUT, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        updatedFeedback = feedbackRepository.findBy_id(savedFeedbck.get_id());
        assertThat(updatedFeedback.getVote(), is(2));

        response = rest.exchange(BASE_URL + "/" + savedFeedbck.get_id() + "/vote", PUT, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        updatedFeedback = feedbackRepository.findBy_id(savedFeedbck.get_id());
        assertThat(updatedFeedback.getVote(), is(3));
    }

    @Test
    public void shouldOrderFeedbackByVoteCountInDesc(){
        feedbackRepository.save(Feedback.builder().text("vote 0").vote(0).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("vote 3").vote(3).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("vote 1").vote(1).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("vote 2").vote(2).creationDate(LocalDate.now()).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "?sort=vote.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().indexOf("\"vote\":3") < response.getBody().indexOf("\"vote\":2"), is(true));
        assertThat(response.getBody().indexOf("\"vote\":2") < response.getBody().indexOf("\"vote\":1"), is(true));
        assertThat(response.getBody().indexOf("\"vote\":1") < response.getBody().indexOf("\"vote\":0"), is(true));

    }

    @Test
    public void shouldOrderFeedbackByCreationDateInDesc(){
        feedbackRepository.save(Feedback.builder().text("second earliest").vote(0).creationDate(LocalDate.now().minusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("most recent").vote(0).creationDate(LocalDate.now().plusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("second most recent").vote(0).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("earliest").vote(0).creationDate(LocalDate.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "?sort=date.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().indexOf("most recent") < response.getBody().indexOf("second most recent"), is(true));
        assertThat(response.getBody().indexOf("second most recent") < response.getBody().indexOf("second earliest"), is(true));
        assertThat(response.getBody().indexOf("second earliest") < response.getBody().indexOf("earliest"), is(true));

    }

    @Test
    public void shouldReturnFeedbackContainsText(){
        feedbackRepository.save(Feedback.builder().text("blue dog").vote(0).creationDate(LocalDate.now().minusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("red dog").vote(0).creationDate(LocalDate.now().plusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("blue cat").vote(0).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("red cat").vote(0).creationDate(LocalDate.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?text=dog", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(true));
        assertThat(response.getBody().contains("red dog"), is(true));
        assertThat(response.getBody().contains("blue cat"), is(false));
        assertThat(response.getBody().contains("red cat"), is(false));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange(BASE_URL + "?text=cat", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(false));
        assertThat(response.getBody().contains("red dog"), is(false));
        assertThat(response.getBody().contains("blue cat"), is(true));
        assertThat(response.getBody().contains("red cat"), is(true));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange(BASE_URL + "?text=blue", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(true));
        assertThat(response.getBody().contains("red dog"), is(false));
        assertThat(response.getBody().contains("blue cat"), is(true));
        assertThat(response.getBody().contains("red cat"), is(false));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange(BASE_URL + "?text=red", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(false));
        assertThat(response.getBody().contains("red dog"), is(true));
        assertThat(response.getBody().contains("blue cat"), is(false));
        assertThat(response.getBody().contains("red cat"), is(true));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange(BASE_URL + "?text=mouse", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().equals("{\"feedback\":[],\"size\":0}"), is(true));
    }

    @Test
    public void shouldReturnFeedbackContainsTextInAnswer(){
        feedbackRepository.save(
                Feedback.builder()
                        .vote(0)
                        .creationDate(LocalDate.now().minusDays(1L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("dog").creationDate(LocalDate.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .vote(0)
                        .creationDate(LocalDate.now().minusDays(1L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("cat").creationDate(LocalDate.now()).build()))
                        .build()
        );

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?text=dog", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("dog"), is(true));
        assertThat(response.getBody().contains("cat"), is(false));
        assertThat(response.getBody().contains("\"size\":1"), is(true));

        response = rest.exchange(BASE_URL + "?text=cat", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("dog"), is(false));
        assertThat(response.getBody().contains("cat"), is(true));
        assertThat(response.getBody().contains("\"size\":1"), is(true));
    }

    @Test
    public void shouldReturnPaginatedItem(){
        feedbackRepository.save(Feedback.builder().text("one").vote(1).creationDate(LocalDate.now().minusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("two").vote(2).creationDate(LocalDate.now().plusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("three").vote(3).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("four").vote(4).creationDate(LocalDate.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=1&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("four"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=3&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(3));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=4&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(4));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=5&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(4));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=1&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("three"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=3&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=4&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=5&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=1&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("two"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=2&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(2));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=3&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(2));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=1&offset=3", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=2&offset=3", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("one"), is(true));
    }

    @Test
    public void shouldReturnPaginatedItemTwo() {
        feedbackRepository.save(Feedback.builder().text("one").vote(1).creationDate(LocalDate.now().minusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("two").vote(2).creationDate(LocalDate.now().plusDays(1L)).build());
        feedbackRepository.save(Feedback.builder().text("three").vote(3).creationDate(LocalDate.now()).build());
        feedbackRepository.save(Feedback.builder().text("four").vote(4).creationDate(LocalDate.now().minusDays(4L)).build());
        feedbackRepository.save(Feedback.builder().text("five").vote(5).creationDate(LocalDate.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=2&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(2));
        assertThat(response.getBody().contains("five"), is(true));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=2&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(2));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));

        response = rest.exchange(BASE_URL + "?sort=vote.desc&limit=2&offset=4", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "_id"), is(1));
        assertThat(response.getBody().contains("one"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));
    }


}