package com.nr.ecommercebe.shared.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIUtil {
    private URIUtil() {
        // Prevent instantiation
    }

    public static URI buildLocationUri(String pathTemplate, Object... uriVariables) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(pathTemplate)
                .buildAndExpand(uriVariables)
                .toUri();
    }
}
