package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.data.PostSimpleData;
import product.domain.post.Post;
import product.domain.post.PostCategory;
import product.mapper.PostSimpleDataMapper;
import product.service.PostService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final PostSimpleDataMapper postSimpleDataMapper;

    @Autowired
    public PostController(PostService postService,
                          PostSimpleDataMapper postSimpleDataMapper) {
        this.postService = postService;
        this.postSimpleDataMapper = postSimpleDataMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER', 'ROLE_ANONYMOUS')")
    @RequestMapping(value = "/{slug}", method= RequestMethod.GET)
    public ResponseEntity findPostByCode(@PathVariable String slug) {
        final Optional<Post> postOptional = postService.getPostBySlug(slug);
        return postOptional.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER', 'ROLE_ANONYMOUS')")
    @RequestMapping(method= RequestMethod.GET)
    public List<PostSimpleData> findPosts(@RequestParam(value = "category", required = false) String categorySlug,
                                          @RequestParam(value = "author", required = false) String authorSlug) {
        return postService.findPublishedPosts(categorySlug, authorSlug)
                .stream().map(postSimpleDataMapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER', 'ROLE_ANONYMOUS')")
    @RequestMapping(value = "/categories/{slug}", method= RequestMethod.GET)
    public ResponseEntity findPostCategoryByCode(@PathVariable String slug){
        final Optional<PostCategory> postCategoryOptional = postService.getPostCategoryBySlug(slug);
        return postCategoryOptional.map(postCategory -> new ResponseEntity<>(postCategory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
