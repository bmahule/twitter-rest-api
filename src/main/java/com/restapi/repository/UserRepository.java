package com.restapi.repository;

import com.restapi.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bmahule on 10/14/17.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM Users u WHERE u.id=:userName",
            nativeQuery = true
    )
    List<User> findUser(String userName);
}