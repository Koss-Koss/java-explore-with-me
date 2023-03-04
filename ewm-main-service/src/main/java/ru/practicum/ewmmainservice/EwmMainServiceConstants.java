package ru.practicum.ewmmainservice;

public class EwmMainServiceConstants {
    public static final String EWM_MAIN_SERVICE_APP_NAME = "ewm-main-service";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ADMIN_PATH = "/admin";
    public static final String USER_PATH = "/users";
    public static final String USER_PREFIX = "/{userId}";
    public static final String CATEGORY_PATH = "/categories";
    public static final String CATEGORY_PREFIX = "/{catId}";
    public static final String EVENT_PATH = "/events";
    public static final String EVENT_PREFIX = "/{eventId}";
    public static final String REQUEST_PATH = "/requests";
    public static final String REQUEST_PREFIX = "/{requestId}";
    public static final String REQUEST_CANCEL_PATH = "/cancel";
    public static final String COMPILATION_PATH = "/compilations";
    public static final String COMPILATION_PREFIX = "/{compId}";
    public static final int COMPILATION_TITLE_LENGTH_MAX = 255;
    public static final int EVENT_ANNOTATION_LENGTH_MIN = 20;
    public static final int EVENT_ANNOTATION_LENGTH_MAX = 2000;
    public static final int EVENT_DESCRIPTION_LENGTH_MIN = 20;
    public static final int EVENT_DESCRIPTION_LENGTH_MAX = 7000;
    public static final int EVENT_TITLE_LENGTH_MIN = 3;
    public static final int EVENT_TITLE_LENGTH_MAX = 120;
    public static final int LIMIT_DATE_EVENT_CHANGE_IN_FUTURE_IN_HOURS = 1;
    public static final int LIMIT_DATE_EVENT_CREATION_IN_FUTURE_IN_HOURS = 2;
    public static final String SORT_PARAM_EVENT_DATE_FOR_EVENT_SEARCH = "EVENT_DATE";
    public static final String SORT_PARAM_VIEWS_FOR_EVENT_SEARCH = "VIEWS";
}
