package com.basecamp.rest.service;


import com.basecamp.rest.domain.Type;
import com.basecamp.rest.repository.TypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeServiceTest {
    @MockBean
    private TypeRepository typeRepository;
    @Autowired
    private TypeService typeService;
    private Type type;

    @Before
    public void setUp() {
        type = new Type();
        type.setName("Type");
        type.setId(1);
    }

    @Test
    public void getAllShouldReturn_AllObjects() {
        List<Type> types = Collections.singletonList(type);
        when(typeRepository.findAll()).thenReturn(types);

        List<Type> all = typeService.getAll();

        assertThat(all).isEqualTo(types);
        verify(typeRepository, times(1)).findAll();
        verifyNoMoreInteractions(typeRepository);
    }

    @Test
    public void getById() {
        when(typeRepository.findOne(type.getId())).thenReturn(type);

        Type byId = typeService.getById(type.getId()).get();

        assertThat(byId).isEqualTo(type);
        verify(typeRepository, times(1)).findOne(type.getId());
        verifyNoMoreInteractions(typeRepository);
    }

    @Test
    public void removeById() {
        doNothing().when(typeRepository).delete(type.getId());

        typeService.removeById(type.getId());

        verify(typeRepository, times(1)).delete(type.getId());
        verifyNoMoreInteractions(typeRepository);
    }

    @Test
    public void addType() {
        when(typeRepository.save(type)).thenReturn(type);

        typeService.addType(type);

        verify(typeRepository, times(1)).save(type);
        verifyNoMoreInteractions(typeRepository);
    }

    @Test
    public void isExists() {
        when(typeRepository.existsByName(type.getName())).thenReturn(true);

        boolean exists = typeService.isExists(type);

        assertThat(exists).isTrue();
        verify(typeRepository, times(1)).existsByName(type.getName());
        verifyNoMoreInteractions(typeRepository);
    }
}