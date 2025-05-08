package com.nr.ecommercebe.shared.constants;

public final class PrivateApi {
    private PrivateApi() {
        // Prevent instantiation
    }

    public static final String API_VERSION = "/api/v1";
    public static final String API_ADMIN = API_VERSION + "/admin/**";
    public static final String API_MEDIA = API_VERSION + "/media/**";
    public static final String API_CURRENT_USER = API_VERSION + "/auth/me";
}
