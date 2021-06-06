package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import product.domain.post.Post;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class PostRepositoryTest extends AbstractRepositoryTest {

    private static final String post_category_insert_sql = "insert into post_category(name, slug) values (?, ?)";
    private static final String author_insert_sql = "insert into author(name, slug) values (?, ?)";
    private static final String post_insert_sql = "insert into post(title, slug, author_id) values(?, ?, ?)";
    private static final String post_category_link_insert_sql = "insert into post_category_link(post_id, post_category_id) values(?, ?)";
    private static final String post_tag_insert_sql = "insert into post_tag(name, slug) values(?, ?)";
    private static final String post_tag_link_insert_sql = "insert into post_tag_link(post_id, post_tag_id) values(?, ?)";
    private static final String post_meta_insert_sql = "insert into post_meta(post_id, name, content) values(?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void shouldGetPostBySlug() {
        // Given
        jdbcTemplate.update(post_category_insert_sql, "Recipe", "recipe");
        final Long postCategoryId = jdbcTemplate.queryForObject("select id from post_category where name = 'Recipe'", Long.class);
        jdbcTemplate.update(author_insert_sql, "Emma", "emma");
        final Long authorId = jdbcTemplate.queryForObject("select id from author where name = 'Emma'", Long.class);
        jdbcTemplate.update(post_insert_sql, "Noodle Recipe", "noodle-recipe", authorId);
        final Long postId = jdbcTemplate.queryForObject("select id from post where title = 'Noodle Recipe'", Long.class);
        jdbcTemplate.update(post_category_link_insert_sql, postId, postCategoryId);
        jdbcTemplate.update(post_tag_insert_sql, "Country", "country");
        final Long postTagId = jdbcTemplate.queryForObject("select id from post_tag where name = 'Country'", Long.class);
        jdbcTemplate.update(post_tag_link_insert_sql, postId, postTagId);
        jdbcTemplate.update(post_meta_insert_sql, postId, "view", "1");

        // When
        Optional<Post> postOptional = postRepository.findBySlug("noodle-recipe");

        // Then
        assertThat(postOptional.isPresent(), is(true));

        // post
        assertThat(postOptional.get().getTitle(), is("Noodle Recipe"));
        assertThat(postOptional.get().getSlug(), is("noodle-recipe"));

        // author
        assertThat(postOptional.get().getAuthor().getName(), is("Emma"));
        assertThat(postOptional.get().getAuthor().getSlug(), is("emma"));

        // categories
        assertThat(postOptional.get().getCategories().size(), is(1));
        assertThat(postOptional.get().getCategories().get(0).getName(), is("Recipe"));
        assertThat(postOptional.get().getCategories().get(0).getSlug(), is("recipe"));

        // post tag
        assertThat(postOptional.get().getTags().size(), is(1));
        assertThat(postOptional.get().getTags().get(0).getName(), is("Country"));
        assertThat(postOptional.get().getTags().get(0).getSlug(), is("country"));

        // post meta
        assertThat(postOptional.get().getMetas().size(), is(1));
        assertThat(postOptional.get().getMetas().get(0).getName(), is("view"));
        assertThat(postOptional.get().getMetas().get(0).getContent(), is("1"));
    }

    @Test
    public void shouldReturnOptionalEmptyIfPostSlugDoesNotExist() {
        // When
        Optional<Post> postOptional = postRepository.findBySlug("unknown");

        // Then
        assertThat(postOptional.isPresent(), is(false));
    }

}