package product.service;

import product.domain.post.Post;

import java.util.Comparator;

public class PostPredicate {

    public static Comparator<Post> createDateDescComparator(){
        return Comparator.comparing(Post::getCreateDate).reversed();
    }
}
