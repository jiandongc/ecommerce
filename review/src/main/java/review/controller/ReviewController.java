package review.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import review.domain.Feedback;
import review.repository.FeedbackRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Feedback getFeedbackById(@PathVariable("id") ObjectId id) {
        return feedbackRepository.findBy_id(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyFeedbackById(@PathVariable("id") ObjectId id, @RequestBody Feedback feedback) {
        feedback.set_id(id);
        feedbackRepository.save(feedback);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        feedback.setCreationDate(LocalDate.now());
        feedbackRepository.save(feedback);
        return feedback;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteFeedback(@PathVariable ObjectId id) {
        feedbackRepository.delete(feedbackRepository.findBy_id(id));
    }

}
