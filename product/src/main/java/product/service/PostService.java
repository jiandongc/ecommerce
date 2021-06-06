package product.service;

import product.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> getPostBySlug(String slug);
    List<Post> findPublishedPosts(String categorySlug, String authorSlug);
}
