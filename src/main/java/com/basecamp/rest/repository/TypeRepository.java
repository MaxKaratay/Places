package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    boolean existsByName(String name);
}
