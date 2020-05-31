package review.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import review.domain.Comment;
import review.domain.Response;
import review.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class CommentControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private CommentService commentService;

    @Test
    public void shouldCreateComment(){
        final String data = "{\"comment\": \"123456\", \"code\": \"site\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(data, headers);
        final ResponseEntity<String> response = rest.exchange("/comments/", POST, payload, String.class);
        assertThat(response.getStatusCode(), is(OK));
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size(), is(1));
        assertThat(comments.get(0).getCode(), is("site"));
        assertThat(comments.get(0).getComment(), is("123456"));
        assertThat(comments.get(0).getVote(), is(0));
        assertThat(comments.get(0).isActive(), is(true));
    }

    @Test
    public void shouldFindCommentById(){
        Comment saved = commentRepository.save(Comment.builder().comment("love your website").code("general").active(true).creationTime(LocalDateTime.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/comments/" + saved.getId(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"comment\":\"love your website\""), is(true));
    }

    @Test
    public void shouldReturn404IfCommentIsInactive(){
        Comment saved = commentRepository.save(Comment.builder().comment("love your website").code("general").active(false).creationTime(LocalDateTime.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/comments/" + saved.getId(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldAddResponseToComment(){
        Comment saved = commentRepository.save(Comment.builder().comment("love your website").code("general").active(true).creationTime(LocalDateTime.now()).build());
        final String answerJson = "{\"response\": \"thank you\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(answerJson, headers);
        final ResponseEntity<String> response = rest.exchange("/comments/" + saved.getId(), PUT, payload, String.class);
        assertThat(response.getStatusCode(), is(OK));
        Comment updateComment = commentService.findById(saved.getId()).get();
        assertThat(updateComment.getComment(), is("love your website"));
        assertThat(updateComment.getResponses().size(), is(1));
        assertThat(updateComment.getResponses().get(0).getResponse(), is("thank you"));
    }

    @Test
    public void shouldReturn404IfTheResponseIsAssociatedToInactiveComment(){
        Comment saved = commentRepository.save(Comment.builder().comment("love your website").code("general").active(false).creationTime(LocalDateTime.now()).build());
        final String answerJson = "{\"response\": \"thank you\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(answerJson, headers);
        final ResponseEntity<String> response = rest.exchange("/comments/" + saved.getId(), PUT, payload, String.class);
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldVoteUpComment(){
        Comment saved = commentRepository.save(Comment.builder().code("site").comment("love your website").active(true).creationTime(LocalDateTime.now()).build());
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rest.exchange("/comments/" + saved.getId() + "/vote", POST, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"vote\":1"), is(true));
        Comment updated = commentRepository.findOne(saved.getId());
        assertThat(updated.getVote(), is(1));

        response = rest.exchange("/comments/" + saved.getId() + "/vote", POST, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"vote\":2"), is(true));
        updated = commentRepository.findOne(saved.getId());
        assertThat(updated.getVote(), is(2));

        response = rest.exchange("/comments/" + saved.getId() + "/vote", POST, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("\"vote\":3"), is(true));
        updated = commentRepository.findOne(saved.getId());
        assertThat(updated.getVote(), is(3));
    }

    @Test
    public void shouldOrderCommentsByVoteCountInDesc(){
        commentRepository.save(Comment.builder().code("site").comment("vote 0").vote(0).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("vote 3").vote(3).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("vote 1").vote(1).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("vote 2").vote(2).active(true).creationTime(LocalDateTime.now()).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange( "/comments?code=site&sort=vote.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().indexOf("\"vote\":3") < response.getBody().indexOf("\"vote\":2"), is(true));
        assertThat(response.getBody().indexOf("\"vote\":2") < response.getBody().indexOf("\"vote\":1"), is(true));
        assertThat(response.getBody().indexOf("\"vote\":1") < response.getBody().indexOf("\"vote\":0"), is(true));
    }

    @Test
    public void shouldOrderCommentByCreationDateInDesc(){
        commentRepository.save(Comment.builder().code("site").comment("second earliest").vote(0).active(true).creationTime(LocalDateTime.now().minusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("most recent").vote(0).active(true).creationTime(LocalDateTime.now().plusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("second most recent").vote(0).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("earliest").vote(0).active(true).creationTime(LocalDateTime.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/comments?code=site&sort=date.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().indexOf("most recent") < response.getBody().indexOf("second most recent"), is(true));
        assertThat(response.getBody().indexOf("second most recent") < response.getBody().indexOf("second earliest"), is(true));
        assertThat(response.getBody().indexOf("second earliest") < response.getBody().indexOf("earliest"), is(true));
    }

    @Test
    public void shouldReturnCommentsContainKeyWord(){
        commentRepository.save(Comment.builder().code("site").comment("dog cat").vote(0).active(false).creationTime(LocalDateTime.now().minusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("blue dog").vote(0).active(true).creationTime(LocalDateTime.now().minusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("red dog").vote(0).active(true).creationTime(LocalDateTime.now().plusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("blue cat").vote(0).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("red cat").vote(0).active(true).creationTime(LocalDateTime.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/comments?code=site&comment=dog", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(true));
        assertThat(response.getBody().contains("red dog"), is(true));
        assertThat(response.getBody().contains("blue cat"), is(false));
        assertThat(response.getBody().contains("red cat"), is(false));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange("/comments?code=site&comment=cat", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(false));
        assertThat(response.getBody().contains("red dog"), is(false));
        assertThat(response.getBody().contains("blue cat"), is(true));
        assertThat(response.getBody().contains("red cat"), is(true));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange("/comments?code=site&comment=blue", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(true));
        assertThat(response.getBody().contains("red dog"), is(false));
        assertThat(response.getBody().contains("blue cat"), is(true));
        assertThat(response.getBody().contains("red cat"), is(false));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange("/comments?code=site&comment=red", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("blue dog"), is(false));
        assertThat(response.getBody().contains("red dog"), is(true));
        assertThat(response.getBody().contains("blue cat"), is(false));
        assertThat(response.getBody().contains("red cat"), is(true));
        assertThat(response.getBody().contains("\"size\":2"), is(true));

        response = rest.exchange("/comments?code=site&comment=mouse", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        System.out.println(response.getBody());

        assertThat(response.getBody().equals("{\"size\":0,\"comment\":[]}"), is(true));
    }

    @Test
    public void shouldReturnCommentsContainKeyWordInResponse(){
        Comment commentOne = Comment.builder()
                .vote(0)
                .active(true)
                .comment("comment")
                .code("site")
                .creationTime(LocalDateTime.now().minusDays(1L))
                .build();
        commentOne.addResponse(Response.builder().response("dog").active(true).vote(0).creationTime(LocalDateTime.now()).build());
        commentRepository.save(commentOne);

        Comment commentTwo = Comment.builder()
                .vote(0)
                .active(true)
                .comment("comment")
                .code("site")
                .creationTime(LocalDateTime.now().minusDays(1L))
                .build();
        commentTwo.addResponse(Response.builder().response("cat").active(true).vote(0).creationTime(LocalDateTime.now()).build());
        commentTwo.addResponse(Response.builder().response("cat 2").active(false).vote(0).creationTime(LocalDateTime.now()).build());
        commentRepository.save(commentTwo);

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/comments?code=site&comment=dog", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("dog"), is(true));
        assertThat(response.getBody().contains("cat"), is(false));
        assertThat(response.getBody().contains("\"size\":1"), is(true));

        response = rest.exchange("/comments?code=site&comment=cat", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().contains("dog"), is(false));
        assertThat(response.getBody().contains("cat"), is(true));
        assertThat(response.getBody().contains("cat 2"), is(false));
        assertThat(response.getBody().contains("\"size\":1"), is(true));
    }

    @Test
    public void shouldReturnPaginatedItem(){
        commentRepository.save(Comment.builder().code("site").comment("one").vote(1).active(true).creationTime(LocalDateTime.now().minusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("two").vote(2).active(true).creationTime(LocalDateTime.now().plusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("three").vote(3).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("four").vote(4).active(true).creationTime(LocalDateTime.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/comments?code=site&sort=vote.desc&limit=1&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("four"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=3&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(3));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=4&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(4));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=5&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(4));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=1&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("three"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=3&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=4&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=5&offset=1", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(3));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=1&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("two"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=2&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(2));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=3&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(2));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=1&offset=3", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("one"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=2&offset=3", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("one"), is(true));
    }

    @Test
    public void shouldReturnPaginatedItemTwo() {
        commentRepository.save(Comment.builder().code("site").comment("one").vote(1).active(true).creationTime(LocalDateTime.now().minusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("two").vote(2).active(true).creationTime(LocalDateTime.now().plusDays(1L)).build());
        commentRepository.save(Comment.builder().code("site").comment("three").vote(3).active(true).creationTime(LocalDateTime.now()).build());
        commentRepository.save(Comment.builder().code("site").comment("four").vote(4).active(true).creationTime(LocalDateTime.now().minusDays(4L)).build());
        commentRepository.save(Comment.builder().code("site").comment("five").vote(5).active(true).creationTime(LocalDateTime.now().minusDays(4L)).build());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/comments?code=site&sort=vote.desc&limit=2&offset=0", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(2));
        assertThat(response.getBody().contains("five"), is(true));
        assertThat(response.getBody().contains("four"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=2&offset=2", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(2));
        assertThat(response.getBody().contains("three"), is(true));
        assertThat(response.getBody().contains("two"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));

        response = rest.exchange("/comments?code=site&sort=vote.desc&limit=2&offset=4", GET, httpEntity, String.class);
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "code"), is(1));
        assertThat(response.getBody().contains("one"), is(true));
        assertThat(response.getBody().contains("\"size\":5"), is(true));
    }


}