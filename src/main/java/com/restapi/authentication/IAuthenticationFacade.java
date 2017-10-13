package com.restapi.authentication;

import org.springframework.security.core.Authentication;

/**
 * Created by bmahule on 10/13/17.
 */

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
