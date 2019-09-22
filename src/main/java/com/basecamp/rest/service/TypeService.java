package com.basecamp.rest.service;

import com.basecamp.rest.domain.Type;
import com.basecamp.rest.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    public Optional<Type> getById(int id) {
        return Optional.ofNullable(typeRepository.findOne(id));
    }

    public void removeById(int id) {
        typeRepository.delete(id);
    }

    public Type addType(Type newType) {
        return typeRepository.save(newType);
    }

    public boolean isExists(Type type) {
        return typeRepository.existsByName(type.getName());
    }
}
