package com.manager.timezone.timezonemanagerserver.constants;

public interface ServerUri {
    String V1_BASE_URI = "/api/v1";

    String TIME_ZONE_BASE_URI = V1_BASE_URI + "/time-zones";
    String USER_BASE_URI = V1_BASE_URI + "/users";

    String RESOURCE_UID_URI = "/{uid}";

    String AUTH_URI = "/auth";
    String SIGN_IN_URI = AUTH_URI + "/sign-in";
    String SIGN_UP_URI = AUTH_URI + "/sign-up";

    String USER_MANAGER_URI = "/managers";
    String USER_MANAGER_SIGN_UP_URI = USER_MANAGER_URI + AUTH_URI + "/sign-up";

    String ADMIN_URI = "/admins";
    String ADMIN_SIGN_UP_URI = ADMIN_URI + AUTH_URI + "/sign-up";
}
