package com.restapi.repository;

import com.restapi.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bmahule on 10/10/17.
 */

@Repository
public interface TweetsRepository extends JpaRepository<Tweet, Integer> {

    @Query(value = "SELECT * FROM tweets t WHERE t.user_name=:userName ORDER BY t.time_stamp ASC",
            nativeQuery = true
    )
    List<Tweet> findByUsername(String userName);
}