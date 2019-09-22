package com.basecamp.rest.service;

import com.basecamp.rest.domain.Review;
import com.basecamp.rest.repository.ReviewRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public Page<Review> getAllForPage(Pageable pageable, String filter) {
        if (StringUtils.isEmpty(filter)) {
            return getAllByPageable(pageable);
        }
        Specification<Review> specification = getSpecificationByFilter(filter);
        return getAllBySpecificationAndPageable(specification, pageable);
    }

    private Specification<Review> getSpecificationByFilter(String filter) {
        String likeFilter = "%" + filter + "%";
        return Specifications.where(getPlaceNameSpecification(likeFilter))
                .or(getPlaceFieldNameSpecification(likeFilter, "city"))
                .or(getPlaceFieldNameSpecification(likeFilter, "type"))
                .or(getPersonNameSpecification(likeFilter))
                .or((review, cq, cb) -> cb.like(review.get("comment"), likeFilter));
    }

    private Specification<Review> getPersonNameSpecification(String likeFilter) {
        return (review, cq, cb) -> cb.like(review.get("person").get("name"), likeFilter);
    }

    private Specification<Review> getPlaceNameSpecification(String likeFilter) {
        return (review, cq, cb) -> cb.like(review.get("place").get("name"), likeFilter);
    }

    private Specification<Review> getPlaceFieldNameSpecification(String likeFilter, String field) {
        return (review, cq, cb) -> cb.like(review.get("place").get(field).get("name"), likeFilter);
    }

    public Page<Review> getAllBySpecificationAndPageable(Specification<Review> specification, Pageable pageable) {
        return reviewRepository.findAll(specification, pageable);
    }

    public Page<Review> getAllByPageable(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

}
