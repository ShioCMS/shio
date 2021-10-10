package com.viglet.shio.spring.security.auth;

import org.springframework.security.core.Authentication;

public interface IShAuthenticationFacade {
    Authentication getAuthentication();
}