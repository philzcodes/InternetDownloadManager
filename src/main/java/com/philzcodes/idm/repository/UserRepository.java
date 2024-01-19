package com.philzcodes.idm.repository;

import org.springframework.data.repository.CrudRepository;

import com.philzcodes.idm.model.User;


public interface UserRepository extends CrudRepository<User, Long> {
 User findByUsername(String username);
}

