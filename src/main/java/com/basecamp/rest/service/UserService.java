package com.basecamp.rest.service;


import com.basecamp.rest.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final PersonService personService;

    @Autowired
    public UserService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> personByName = personService.findPersonByName(userName);
        return personByName.orElseThrow(() -> new UsernameNotFoundException("Person with name " + userName + "not found !"));
    }
}
