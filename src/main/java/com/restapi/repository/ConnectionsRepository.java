package com.restapi.repository;

import com.restapi.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bmahule on 10/10/17.
 */

@Repository
public interface ConnectionsRepository extends CrudRepository<Connection, Integer> {

    @Query(value = "SELECT * FROM Connection conn WHERE conn.follower_id=:followerId and conn.followee_id=:followeeId",
            nativeQuery = true
    )
    List<Connection> findConnection(String followerId, String followeeId);

    @Query(value = "SELECT * FROM Connection conn WHERE conn.follower_id=:followerId",
            nativeQuery = true
    )
    List<Connection> findConnectionsForUser(String followerId);
}