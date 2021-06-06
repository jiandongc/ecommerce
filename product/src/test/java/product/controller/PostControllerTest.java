package product.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.data.PostSimpleData;
import product.domain.post.*;
import product.repository.AuthorRepository;
import product.repository.PostCategoryRepository;
import product.repository.PostRepository;
import product.repository.PostTagRepository;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpMethod.GET;

public class PostControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void shouldGetProductBySlug(){
        // Given
        Author author = Author.builder().name("Emma").slug("emma").build();
        authorRepository.save(author);

        PostCategory postCategory = PostCategory.builder().name("Recipe").slug("recipe").build();
        postCategoryRepository.save(postCategory);

        PostTag postTag = PostTag.builder().name("Japanese Recipe").slug("japanese-recipe").build();
        postTagRepository.save(postTag);

        PostMeta postMeta = PostMeta.builder().name("star").content("5").build();

        Post post = Post.builder().title("Noodle Recipe").slug("noodle-recipe").build();
        post.addPostCategory(postCategory);
        post.addTag(postTag);
        post.setAuthor(author);
        post.addMeta(postMeta);
        postRepository.save(post);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Post> response = restTemplate.exchange("/posts/noodle-recipe", GET, httpEntity, Post.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Post actual = response.getBody();
        assertThat(actual.getTitle(), is("Noodle Recipe"));
        assertThat(actual.getAuthor().getName(), is("Emma"));
        assertThat(actual.getCategories().size(), is(1));
        assertThat(actual.getCategories().get(0).getName(), is("Recipe"));
        assertThat(actual.getTags().size(), is(1));
        assertThat(actual.getTags().get(0).getName(), is("Japanese Recipe"));
        assertThat(actual.getMetas().size(), is(1));
        assertThat(actual.getMetas().get(0).getName(), is("star"));
        assertThat(actual.getMetas().get(0).getContent(), is("5"));
    }

    @Test
    public void shouldReturn404IfPostCanNotBeFound(){
        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Post> response = restTemplate.exchange("/posts/unknown", GET, httpEntity, Post.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldGetAllPublishedPostsInReverseOrderOfCreateDate(){
        // Given
        Author author = Author.builder().name("Emma").slug("emma").build();
        authorRepository.save(author);

        PostCategory postCategory = PostCategory.builder().name("Recipe").slug("recipe").build();
        postCategoryRepository.save(postCategory);

        Post firstPost = Post.builder().title("Taiwan Noodle Recipe").slug("taiwan-noodle-recipe").published(true).build();
        firstPost.addPostCategory(postCategory);
        firstPost.setAuthor(author);
        firstPost.setCreateDate(LocalDate.of(2011, 1, 1));
        postRepository.save(firstPost);

        Post secondPost = Post.builder().title("Japanese Noodle Recipe").slug("japanese-noodle-recipe").published(false).build();
        secondPost.addPostCategory(postCategory);
        secondPost.setAuthor(author);
        secondPost.setCreateDate(LocalDate.of(2011, 6, 1));
        postRepository.save(secondPost);

        Post thirdPost = Post.builder().title("Korean Noodle Recipe").slug("korean-noodle-recipe").published(true).build();
        thirdPost.addPostCategory(postCategory);
        thirdPost.setAuthor(author);
        thirdPost.setCreateDate(LocalDate.of(2011, 12, 31));
        postRepository.save(thirdPost);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<PostSimpleData[]> response = restTemplate.exchange("/posts", GET, httpEntity, PostSimpleData[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        PostSimpleData[] actual = response.getBody();
        assertThat(actual.length, is(2));
        assertThat(actual[0].getTitle(), is("Korean Noodle Recipe"));
        assertThat(actual[1].getTitle(), is("Taiwan Noodle Recipe"));
    }

    @Test
    public void shouldGetPublishedPostsByCategory(){
        // Given
        Author author = Author.builder().name("Emma").slug("emma").build();
        authorRepository.save(author);

        PostCategory recipeCategory = PostCategory.builder().name("Recipe").slug("recipe").build();
        postCategoryRepository.save(recipeCategory);

        PostCategory infoCategory = PostCategory.builder().name("Info").slug("info").build();
        postCategoryRepository.save(infoCategory);

        Post firstPost = Post.builder().title("Taiwan Noodle Recipe").slug("taiwan-noodle-recipe").published(true).build();
        firstPost.addPostCategory(recipeCategory);
        firstPost.setAuthor(author);
        firstPost.setCreateDate(LocalDate.of(2011, 1, 1));
        postRepository.save(firstPost);

        Post secondPost = Post.builder().title("Japanese Noodle Recipe").slug("japanese-noodle-recipe").published(false).build();
        secondPost.addPostCategory(recipeCategory);
        secondPost.setAuthor(author);
        secondPost.setCreateDate(LocalDate.of(2011, 6, 1));
        postRepository.save(secondPost);

        Post thirdPost = Post.builder().title("Korean Noodle Recipe").slug("korean-noodle-recipe").published(true).build();
        thirdPost.addPostCategory(infoCategory);
        thirdPost.setAuthor(author);
        thirdPost.setCreateDate(LocalDate.of(2011, 12, 31));
        postRepository.save(thirdPost);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<PostSimpleData[]> response = restTemplate.exchange("/posts?category=recipe", GET, httpEntity, PostSimpleData[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        PostSimpleData[] actual = response.getBody();
        assertThat(actual.length, is(1));
        assertThat(actual[0].getTitle(), is("Taiwan Noodle Recipe"));
    }

    @Test
    public void shouldGetPublishedPostByAuthor(){
        // Given
        Author emma = Author.builder().name("Emma").slug("emma").build();
        authorRepository.save(emma);

        Author jack = Author.builder().name("Jack").slug("jack").build();
        authorRepository.save(jack);

        PostCategory postCategory = PostCategory.builder().name("Recipe").slug("recipe").build();
        postCategoryRepository.save(postCategory);

        Post firstPost = Post.builder().title("Taiwan Noodle Recipe").slug("taiwan-noodle-recipe").published(true).build();
        firstPost.addPostCategory(postCategory);
        firstPost.setAuthor(emma);
        firstPost.setCreateDate(LocalDate.of(2011, 1, 1));
        postRepository.save(firstPost);

        Post secondPost = Post.builder().title("Japanese Noodle Recipe").slug("japanese-noodle-recipe").published(false).build();
        secondPost.addPostCategory(postCategory);
        secondPost.setAuthor(emma);
        secondPost.setCreateDate(LocalDate.of(2011, 6, 1));
        postRepository.save(secondPost);

        Post thirdPost = Post.builder().title("Korean Noodle Recipe").slug("korean-noodle-recipe").published(true).build();
        thirdPost.addPostCategory(postCategory);
        thirdPost.setAuthor(jack);
        thirdPost.setCreateDate(LocalDate.of(2011, 12, 31));
        postRepository.save(thirdPost);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<PostSimpleData[]> response = restTemplate.exchange("/posts?author=emma", GET, httpEntity, PostSimpleData[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        PostSimpleData[] actual = response.getBody();
        assertThat(actual.length, is(1));
        assertThat(actual[0].getTitle(), is("Taiwan Noodle Recipe"));
    }

}