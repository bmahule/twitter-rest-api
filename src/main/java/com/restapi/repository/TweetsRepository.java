package com.restapi.repository;

import com.restapi.model.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bmahule on 10/10/17.
 */

@Repository
public interface TweetsRepository extends JpaRepository<Tweets, Integer>{
}