package review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import review.domain.Comment;
import review.domain.Response;
import review.repository.CommentRepository;
import review.service.CommentService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public Map findComments(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) {

        List<Comment> comments = commentRepository.findByCode(code);
        int size = comments.size();

        if (comment != null) {
            comments = comments.stream()
                    .filter(item -> (item.getComment() != null && item.getComment().contains(comment))
                            || (item.getResponses() != null && item.getResponses().stream().anyMatch(response -> response.getResponse() != null && response.getResponse().contains(comment))))
                    .collect(Collectors.toList());
            size = comments.size();
        }

        if (sort != null && sort.equalsIgnoreCase("vote.desc")) {
            comments = comments.stream().sorted(Comparator.comparing(Comment::getVote).reversed()).collect(Collectors.toList());
        }

        if (sort != null && sort.equalsIgnoreCase("date.desc")) {
            comments = comments.stream().sorted(Comparator.comparing(Comment::getCreationTime).reversed()).collect(Collectors.toList());
        }

        if (limit != null && offset != null && offset < comments.size()) {
            comments = comments.subList(offset, (offset+limit) < comments.size() ? offset+limit : comments.size());
        }

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("size", size);
        returnData.put("comment", comments);
        return returnData;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") long id) {
        Optional<Comment> commentOptional = commentService.findById(id);
        return commentOptional.map(o -> new ResponseEntity<>(o, OK)).orElse(new ResponseEntity<>(NOT_FOUND));

    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity addResponse(@PathVariable("id") long id, @RequestBody Response response) {
        Comment comment = commentRepository.findOne(id);
        if (comment != null) {
            response.setVote(0);
            response.setActive(true);
            response.setCreationTime(LocalDateTime.now());
            comment.addResponse(response);
            Comment updated = commentRepository.save(comment);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}/vote", method = RequestMethod.POST)
    public ResponseEntity addVote(@PathVariable("id") long id){
        Comment comment = commentRepository.findOne(id);
        if (comment != null) {
            comment.voteUp();
            Comment updated = commentRepository.save(comment);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public Comment createComment(@RequestBody Comment comment) {
        comment.setVote(0);
        comment.setActive(true);
        comment.setCreationTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

}
