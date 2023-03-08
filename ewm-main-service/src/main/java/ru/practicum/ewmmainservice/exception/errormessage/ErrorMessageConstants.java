package ru.practicum.ewmmainservice.exception.errormessage;

import ru.practicum.ewmmainservice.event.model.EventState;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;

public class ErrorMessageConstants {
    public static final String ADDING_REPEAT_REQUEST_MESSAGE = "Нельзя повторно запрашивать участие в событии";
    public static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Уже существует категория с name = ";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Запрос на несуществующую категорию с id = ";
    public static final String COMPILATION_ALREADY_EXISTS_MESSAGE = "Уже существует подборка событий с title = ";
    public static final String COMPILATION_NOT_FOUND_MESSAGE = "Запрос на несуществующую подборку событий с id = ";
    public static final String COMPILATION_TITLE_NOT_BLANK_MESSAGE =
            "У подборки событий title - обязательное поле, " +
                    "которое не может быть пустым или состоять из одних пробелов";
    public static final String COMPILATION_TITLE_SIZE_INCORRECT_MESSAGE =
            "У подборки событий значение title должно иметь длину не более " + COMPILATION_TITLE_LENGTH_MAX +
                    " символов";
    public static final String DELETE_CATEGORY_WITH_RELATED_EVENTS_MESSAGE =
            "Нельзя удалять категорию при наличии связанных с ней событий";
    public static final String DELETE_REGION_TYPE_WITH_RELATED_REGIONS_MESSAGE =
            "Нельзя удалять тип региона при наличии связанных с ним регионов";
    public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Уже существует пользователь с email = ";
    public static final String EVENT_ANNOTATION_SIZE_INCORRECT_MESSAGE =
            "У события значение annotation должно иметь длину в интервале [" + EVENT_ANNOTATION_LENGTH_MIN + ", " +
                    EVENT_ANNOTATION_LENGTH_MAX + "]";
    public static final String EVENT_DESCRIPTION_SIZE_INCORRECT_MESSAGE =
            "У события значение description должно иметь длину в интервале [" + EVENT_DESCRIPTION_LENGTH_MIN + ", " +
                    EVENT_DESCRIPTION_LENGTH_MAX + "]";
    public static final String EVENT_NOT_AVAILABLE_FOR_VIEWING_MESSAGE = "Недопуступно для просмотра событие с id = ";
    public static final String EVENT_NOT_FOUND_MESSAGE = "Запрос на несуществующее событие с id = ";
    public static final String EVENT_TITLE_SIZE_INCORRECT_MESSAGE =
            "У события значение title должно иметь длину в интервале [" + EVENT_TITLE_LENGTH_MIN + ", " +
                    EVENT_TITLE_LENGTH_MAX + "]";
    public static final String INCORRECT_EVENT_DATE_CHANGED_MESSAGE =
            "Дата/время начала события должно быть не ранее, чем за " +
                    LIMIT_DATE_EVENT_CHANGE_IN_FUTURE_IN_HOURS + "час от даты публикации";
    public static final String INCORRECT_EVENT_DATE_CHANGED_NOW_BEFORE_MESSAGE =
            "Дата/время начала события не может быть ранее уже наступившего момента";
    public static final String INCORRECT_EVENT_DATE_CREATED_MESSAGE =
            "Дата/время начала события должно быть не ранее, чем через " +
                    LIMIT_DATE_EVENT_CREATION_IN_FUTURE_IN_HOURS + "часа после его создания";
    public static final String INCORRECT_LATITUDE_VALUE_MESSAGE =
            "Значение широты должно быть в интервале [-90, 90] градусов";
    public static final String INCORRECT_LONGITUDE_VALUE_MESSAGE =
            "Значение долготы должно быть в интервале [0, 180] градусов";
    public static final String INCORRECT_RADIUS_VALUE_MESSAGE =
            "Значение радиуса должно быть в интервале [0, 20000] километров";
    public static final String INCORRECT_SORT_PARAM_MESSAGE = "Недопустимый параметр сортировки sort = ";
    public static final String LOCATION_NOT_FOUND_MESSAGE = "Запрос на несуществующую локацию с id = ";
    public static final String NOT_CANCELED_EVENT_ADMIN_MESSAGE =
            "Событие не может быть отклонено, находясь в статусе " + EventState.PUBLISHED;
    public static final String NOT_CANCELED_EVENT_INITIATOR_MESSAGE =
            "Пользователь может отменить своё событие только на стадии публикации";
    public static final String NOT_PUBLISHED_EVENT_ADMIN_MESSAGE =
            "Событие не может быть опубликовано, не находясь в статусе " + EventState.PENDING;
    public static final String NULL_LOCATION_LATITUDE_MESSAGE = "Не указана широта локации";
    public static final String NULL_LOCATION_LONGITUDE_MESSAGE = "Не указана долгота локации";
    public static final String NULL_REGION_LATITUDE_MESSAGE = "Не указана широта региона";
    public static final String NULL_REGION_LONGITUDE_MESSAGE = "Не указана долгота региона";
    public static final String NULL_REGION_RADIUS_MESSAGE = "Не указан радиус региона";
    public static final String PARTICIPATION_IN_PENDING_EVENT_MESSAGE =
            "Нельзя запрашивать участие в неопубликованном событии";
    public static final String RANGE_START_AFTER_RANGE_END_MESSAGE =
            "Начало диапазона даты/времени не может быть позже его окончания";
    public static final String REACHED_LIMIT_OF_PARTICIPANTS_MESSAGE =
            "Нельзя одобрить заявку на участие: уже достигнут лимит на участников для события с id = ";
    public static final String REGION_DESCRIPTION_SIZE_INCORRECT_MESSAGE =
            "У региона значение description должно иметь длину не более " +
                    REGION_DESCRIPTION_LENGTH_MAX + " символов";
    public static final String REGION_NOT_FOUND_MESSAGE = "Запрос на несуществующий регион с id = ";
    public static final String REGION_TYPE_ALREADY_EXISTS_MESSAGE = "Уже существует тип региона с name = ";
    public static final String REGION_TYPE_NOT_FOUND_MESSAGE = "Запрос на несуществующий тип региона с id = ";
    public static final String REJECT_ALREADY_CONFIRMED_REQUEST_MESSAGE =
            "Нельзя отклонить уже одобренную заявку на участие с id = ";
    public static final String REQUEST_LIMIT_REACHED_MESSAGE =
            "Уже достигнут лимит запросов на участие в событии с id = ";
    public static final String REQUEST_NOT_FOUND_MESSAGE = "Запрос на несуществующую заявку на участие с id = ";
    public static final String REQUESTER_EQUALS_INITIATOR_MESSAGE =
            "Инициатор события не может запрашивать участие в нём";
    public static final String UPDATE_REQUEST_WITHOUT_PENDING_STATUS_MESSAGE =
            "Нельзя изменить статус у заявки на участие не в статусе " + EventState.PENDING + " для заявки с id = ";
    public static final String USER_EMAIL_INCORRECT_MESSAGE = "Указан некорректный email пользователя";
    public static final String USER_GET_NOT_EVENT_INITIATOR_MESSAGE =
            "Запрашивающий информацию пользователь не являетс инициатором события с id = ";
    public static final String USER_NOT_EVENT_INITIATOR_MESSAGE = "Редактировать событие может только его инициатор";
    public static final String USER_NOT_FOUND_MESSAGE = "Запрос на несуществующего пользователя с id = ";
    public static final String USER_NOT_EMAIL_MESSAGE = "Не указан email пользователя";
    public static final String USER_NOT_NAME_MESSAGE = "Не указано имя (name) пользователя";

}
