package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.Role;
import com.basecamp.rest.domain.Type;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.service.TypeService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TypeControllerTest extends ControllerTest {
    @MockBean
    private TypeService typeService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${jwt.key}")
    private String key;

    @Test
    public void testGet() {
        Type type = new Type();
        type.setId(1);
        type.setName("Type");

        given(typeService.getById(1)).willReturn(Optional.of(type));

        ResponseEntity<Type> entity = restTemplate.getForEntity("/types/1", Type.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(type);
        verify(typeService, times(1)).getById(1);
        verifyNoMoreInteractions(typeService);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        Type type = new Type();
        Type type2 = new Type();
        type.setId(1);
        type2.setId(2);
        type.setName("Type");
        type2.setName("TYpe");

        List<Type> types = Arrays.asList(type, type2);

        given(typeService.getAll()).willReturn(types);

        ResponseEntity<CollectionWrapper<Type>> entity = restTemplate.exchange("/types", HttpMethod.GET,
                null, new ParameterizedTypeReference<CollectionWrapper<Type>>() {});

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new CollectionWrapper<>(types));
        verify(typeService, times(1)).getAll();
        verifyNoMoreInteractions(typeService);
    }

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        Type type = new Type();
        type.setName("Type");
        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<Type> httpEntity = new HttpEntity<>(type, headers);


        given(typeService.addType(type)).willReturn(type);

        ResponseEntity<Type> entity = restTemplate.postForEntity("/types", httpEntity, Type.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(type);
        verify(typeService, times(1)).addType(type);
        verifyNoMoreInteractions(typeService);
    }

    @Test
    public void delete_ShouldReturnStatusOk() {
        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<Type> httpEntity = new HttpEntity<>(null, headers);


        doNothing().when(typeService).removeById(1);

        ResponseEntity<Object> entity = restTemplate.exchange("/types/1", HttpMethod.DELETE, httpEntity, Object.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(typeService, times(1)).removeById(1);
        verifyNoMoreInteractions(typeService);
    }
}