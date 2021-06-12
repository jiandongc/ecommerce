package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.domain.post.Author;
import product.domain.post.Post;
import product.domain.post.PostCategory;
import product.repository.AuthorRepository;
import product.repository.PostCategoryRepository;
import product.repository.PostRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostCategoryRepository postCategoryRepository;

    private final AuthorRepository authorRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           PostCategoryRepository postCategoryRepository,
                           AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.postCategoryRepository = postCategoryRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findPublishedPosts(String categorySlug, String authorSlug) {
        List<Post> posts;

        if (categorySlug != null) {
            posts = postCategoryRepository.findBySlug(categorySlug)
                    .map(PostCategory::getPosts)
                    .orElse(Collections.emptyList());
        } else if (authorSlug != null) {
            posts = authorRepository.findBySlug(authorSlug)
                    .map(Author::getPosts)
                    .orElse(Collections.emptyList());
        } else {
            posts = postRepository.findAll();
        }

        return posts.stream()
                .filter(Post::getPublished)
                .sorted(PostPredicate.createDateDescComparator())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostCategory> getPostCategoryBySlug(String slug) {
        return postCategoryRepository.findBySlug(slug);
    }
}
