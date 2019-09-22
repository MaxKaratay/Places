package com.basecamp.rest.model;

import com.basecamp.rest.domain.Review;
import com.basecamp.rest.service.PlaceService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ReviewModel {
    private int placeId;
    @Min(value = 1, message = "Rating should be grater than 0")
    @Max(value = 10, message = "Rating should be less than 11")
    private int rating;
    @Size(min = 10, max = 255, message = "We want to know your point about this place, let us a little message, it could be from 10 to 255 characters")
    private String comment;

    public Review toReview(PlaceService placeService) {
        Review review = new Review();
        review.setPlace(placeService.getById(placeId).get());
        review.setRating(rating);
        review.setComment(comment);
        return review;
    }
}
