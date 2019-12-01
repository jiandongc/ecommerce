package review.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import review.domain.Answer;
import review.domain.Feedback;
import review.repository.FeedbackRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public Map findFeedbacks(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) {

        List<Feedback> feedback = feedbackRepository.findAll();
        int size = feedback.size();

        if (text != null) {
            feedback = feedback.stream()
                    .filter(item -> (item.getText() != null && item.getText().contains(text))
                            || (item.getAnswers() != null && item.getAnswers().stream().anyMatch(answer -> answer.getText() != null && answer.getText().contains(text))))
                    .collect(Collectors.toList());
            size = feedback.size();
        }

        if (sort != null && sort.equalsIgnoreCase("vote.desc")) {
            feedback = feedback.stream().sorted(Comparator.comparing(Feedback::getVote).reversed()).collect(Collectors.toList());
        }

        if (sort != null && sort.equalsIgnoreCase("date.desc")) {
            feedback = feedback.stream().sorted(Comparator.comparing(Feedback::getCreationDate).reversed()).collect(Collectors.toList());
        }

        if (limit != null && offset != null && offset < feedback.size()) {
            feedback = feedback.subList(offset, (offset+limit) < feedback.size() ? offset+limit : feedback.size());
        }

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("size", size);
        returnData.put("feedback", feedback);
        return returnData;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Feedback getFeedbackById(@PathVariable("id") ObjectId id) {
        return feedbackRepository.findBy_id(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void addAnswer(@PathVariable("id") ObjectId id, @RequestBody Answer answer) {
        Feedback feedback = feedbackRepository.findBy_id(id);
        if (feedback != null) {
            answer.setCreationDate(LocalDate.now());
            feedback.addAnswer(answer);
            feedbackRepository.save(feedback);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{id}/vote", method = RequestMethod.PUT)
    public void addVote(@PathVariable("id") ObjectId id){
        Feedback feedback = feedbackRepository.findBy_id(id);
        if (feedback != null) {
            feedback.addVote();
            feedbackRepository.save(feedback);
        }
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
