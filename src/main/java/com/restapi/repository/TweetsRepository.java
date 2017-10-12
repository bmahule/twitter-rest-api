package com.restapi.repository;

import com.restapi.model.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bmahule on 10/10/17.
 */

@Repository
public interface TweetsRepository extends CrudRepository<Tweet, Integer> {
}