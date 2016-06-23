package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * Created by Sheremet on 20.06.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceImplTest {
    @Autowired
    protected UserMealService service;
    @Autowired
    private DbPopulator dbPopulator;
    private Comparator<UserMeal> compareByTime = (UserMeal um1, UserMeal um2) ->
            um2.getDateTime().compareTo(um1.getDateTime());
    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        UserMeal mealFromSQL = service.get(START_SEQ+8,ADMIN_ID);
        UserMeal mealFromMap = MEAL_MAP.get(ADMIN_ID).get(1);
        MATCHER.assertEquals(mealFromMap, mealFromSQL);
    }
    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(START_SEQ+8, USER_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(START_SEQ+6,USER_ID);
        List<UserMeal> meals = new ArrayList<>(MEAL_MAP.get(USER_ID));
        meals.remove(4);
        Collections.sort(meals, compareByTime);
        MATCHER.assertCollectionEquals(meals,service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound () throws Exception {
        service.delete(START_SEQ+2, ADMIN_ID);
    }

    @Test
    public void testGetBetweenDate() throws Exception {
        LocalDate from = LocalDate.of(2015,05,30);
        LocalDate to = LocalDate.of(2015,05,31);
        MATCHER.assertCollectionEquals(getFiltered(from, to, USER_ID),service.getBetweenDates(from, to, USER_ID));
    }

/*    @Test
    public void testGetBetweenTime() throws Exception {
        LocalTime from = LocalTime.of(9,00);
        LocalTime to = LocalTime.of(11,00);
        MATCHER.assertCollectionEquals(getFilteredByTime(), service.getBetweenDateTimes(from, to, USER_ID));
    }*/

    @Test
    public void testGetAll() throws Exception {
        Collection<UserMeal> all = service.getAll(ADMIN_ID);
        List<UserMeal> meals = new ArrayList<>(MEAL_MAP.get(ADMIN_ID));
        Collections.sort(meals, compareByTime);
        MATCHER.assertCollectionEquals(meals, all);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() throws Exception {
        UserMeal updatedMeal = service.get(START_SEQ+8, USER_ID);
        service.update(updatedMeal, ADMIN_ID);
    }

    @Test
    public void testSave() throws Exception {
        UserMeal meal = service.save(new UserMeal(null, LocalDateTime.now(), "new meal", 500), USER_ID);
        List<UserMeal> meals = new ArrayList<>(MEAL_MAP.get(USER_ID));
        meals.add(0, meal);
        Collections.sort(meals, compareByTime);
        MATCHER.assertCollectionEquals(meals, service.getAll(USER_ID));
    }
    @Test(expected = DataAccessException.class)
    public void testDuplicationDateTimeSave() throws Exception {
        service.save(new UserMeal(null, LocalDateTime.of(2016,05,30,10,00,38), "Duplicate", 1610), USER_ID);
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updatedMeal = MEAL_MAP.get(USER_ID).get(1);
        updatedMeal.setDescription("some other desc");
        updatedMeal.setCalories(3000);
        updatedMeal.setDateTime(LocalDateTime.now());
        service.update(updatedMeal, USER_ID);
        MATCHER.assertEquals(updatedMeal, service.get(START_SEQ+3, USER_ID));
    }
}