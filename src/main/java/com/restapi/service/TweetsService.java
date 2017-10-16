package com.restapi.service;

import com.restapi.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by bmahule on 10/15/17.
 */

public interface TweetsService {
    Page<Tweet> listAllByPage(Pageable pageable);
}
