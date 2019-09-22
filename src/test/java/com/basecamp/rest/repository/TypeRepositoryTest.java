package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Type;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TypeRepository repository;
    private Type type;

    @Before
    public void setUp() {
        type = new Type();
        type.setName("Type");
    }

    @Test
    public void saveMethod_ShouldSaveObject() {
        Type save = repository.save(type);

        assertThat(save.getId()).isNotNull();
        assertThat(save).isEqualTo(type);
    }

    @Test
    public void findMethod_ShouldFindObject() {
        Integer id = entityManager.persistAndGetId(type, Integer.class);
        Type one = repository.findOne(id);

        assertThat(one).isNotNull();
        assertThat(one).isEqualTo(type);
    }

    @Test
    public void findAll_ShouldReturnAllObjects() {
        Type type1 = new Type();
        type1.setName("TypeA");
        List<Type> types = Arrays.asList(type1, type);

        types.forEach(entityManager::persistAndFlush);
        List<Type> all = repository.findAll();

        assertThat(all).isNotNull();
        assertThat(all).isEqualTo(types);
    }

    @Test
    public void delete_ShouldRemoveObject() {
        Integer id = entityManager.persistAndGetId(type, Integer.class);

        repository.delete(id);
        Type one = repository.findOne(id);

        assertThat(one).isNull();
    }

    @Test
    public void existsByName_ShouldReturnTrue_IfReviewExists() {
        entityManager.persistAndFlush(type);
        boolean exists = repository.existsByName(type.getName());

        assertThat(exists).isTrue();
    }

    @Test
    public void existsByName_ShouldReturnFalse_IfReviewDoesntExists() {
        boolean exists = repository.existsByName(type.getName());

        assertThat(exists).isFalse();
    }

}