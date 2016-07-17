package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static org.junit.Assert.*;

/**
 * Created by Sheremet on 16.07.2016.
 */
public class UserMealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserMealRestController.REST_URL + '/';

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEEDED.contentListMatcher(UserMealsUtil.getWithExceeded(USER_MEALS, 2000))));
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = new UserMeal(MEAL1_ID, LocalDateTime.now(), "test", 123);
        mockMvc.perform(put(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertEquals(updated, mealService.get(MEAL1_ID, USER_ID));
}

    @Test
    public void testCreate() throws Exception {
        UserMeal expected = new UserMeal(LocalDateTime.now(), "test", 321);
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected))).andExpect(status().isCreated());

        UserMeal returned = MATCHER.fromJsonAction(actions);
        expected.setId(returned.getId());

        MATCHER.assertEquals(expected, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetBetween() throws Exception {
        List<UserMealWithExceed> exceedList = UserMealsUtil.getWithExceeded(USER_MEALS, 2000);
        mockMvc.perform(get(REST_URL + "between?start=2015-05-31T09:15:30&end=2015-05-31T14:15:30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEEDED.contentListMatcher(Arrays.asList(exceedList.get(1), exceedList.get(2))));
    }
}