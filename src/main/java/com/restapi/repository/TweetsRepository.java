package com.restapi.repository;

import com.restapi.model.Tweet;
import com.restapi.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bmahule on 10/10/17.
 */

@Repository
public interface TweetsRepository extends CrudRepository<Tweet, Integer> {

    @Query(value = "SELECT * FROM tweets t WHERE t.user_name=:userName ORDER BY t.time_stamp ASC",
            nativeQuery = true
    )
    Iterable<Tweet> findAll(String userName);
}