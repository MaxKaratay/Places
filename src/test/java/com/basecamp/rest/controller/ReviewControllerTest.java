package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.Place;
import com.basecamp.rest.domain.Review;
import com.basecamp.rest.domain.Role;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.ReviewModel;
import com.basecamp.rest.service.PlaceService;
import com.basecamp.rest.service.ReviewService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReviewControllerTest extends ControllerTest {
    @MockBean
    private PlaceService placeService;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${jwt.key}")
    private String key;


    @Test
    public void getAll_ShouldReturnAllObjects() {
        List<Review> reviews = Arrays.asList(new Review(), new Review());

        given(reviewService.getAll()).willReturn(reviews);
        ResponseEntity<CollectionWrapper<Review>> entity = restTemplate
                .exchange("/reviews", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionWrapper<Review>>() {});

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new CollectionWrapper<>(reviews));
        verify(reviewService, times(1)).getAll();
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        ReviewModel model = new ReviewModel();
        model.setPlaceId(1);
        model.setComment("test comment");
        model.setRating(1);
        Review review = new Review();

        String token = getToken(key, Collections.singleton(Role.USER));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<ReviewModel> httpEntity = new HttpEntity<>(model, headers);

        given(placeService.getById(1)).willReturn(Optional.of(new Place()));
        given(reviewService.addReview(any())).willReturn(review);

        ResponseEntity<Review> entity = restTemplate.postForEntity("/reviews", httpEntity, Review.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(review);
        verify(reviewService, times(1)).addReview(any());
        verifyNoMoreInteractions(reviewService);
    }
}