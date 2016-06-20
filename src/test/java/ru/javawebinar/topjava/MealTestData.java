package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.util.*;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;
/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final Map<Integer, List<UserMeal>> MEAL_MAP = new HashMap<>();
    static {
        MEAL_MAP.put(USER_ID, Arrays.asList(
            new UserMeal(START_SEQ+2, LocalDateTime.of(2016,05,30,10,00,38), "Завтрак", 610),
            new UserMeal(START_SEQ+3, LocalDateTime.of(2015,05,30,15,36,38), "Обед", 510),
            new UserMeal(START_SEQ+4, LocalDateTime.of(2015,05,30,20,36,38), "Ужин", 1000),
            new UserMeal(START_SEQ+5, LocalDateTime.of(2015,05,31,10,36,38), "Завтрак", 510),
            new UserMeal(START_SEQ+6, LocalDateTime.of(2015,05,31,15,36,38), "salo", 1000)
        ));
        MEAL_MAP.put(ADMIN_ID, Arrays.asList(
            new UserMeal(START_SEQ+7, LocalDateTime.of(2015,06,1,15,36,38), "Админ ланч", 510),
            new UserMeal(START_SEQ+8, LocalDateTime.of(2015,06,1,20,10,8), "Админ ужин", 1510)
        ));
    }

    public static final ModelMatcher<UserMeal, String> MATCHER = new ModelMatcher<>(UserMeal::toString);

}
