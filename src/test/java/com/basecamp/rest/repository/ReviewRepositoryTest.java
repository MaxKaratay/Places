package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Review;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ReviewRepository repository;
    private Review review;

    @Before
    public void init() {
        review = new Review();
        review.setRating(1);
        review.setComment("Review comment");
    }

    @Test
    public void saveMethod_ShouldSaveObject() {
        Review save = repository.save(review);

        assertThat(save.getId()).isNotNull();
        assertThat(save).isEqualTo(review);
    }

    @Test
    public void findMethod_ShouldFindObject() {
        Integer id = entityManager.persistAndGetId(this.review, Integer.class);
        Review one = repository.findOne(id);

        assertThat(one).isNotNull();
        assertThat(one).isEqualTo(review);
    }

    @Test
    public void findAll_ShouldReturnAllObjects() {
        Review review1 = new Review();
        review1.setComment("Next comment");
        review1.setRating(1);
        List<Review> reviews = Arrays.asList(review1, review);

        reviews.forEach(entityManager::persistAndFlush);
        List<Review> all = repository.findAll();

        assertThat(all).isNotNull();
        assertThat(all).isEqualTo(reviews);
    }

    @Test
    public void delete_ShouldRemoveObject() {
        Integer id = entityManager.persistAndGetId(review, Integer.class);

        repository.delete(id);
        Review one = repository.findOne(id);

        assertThat(one).isNull();
    }
}