package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * User: gkislin
 * Date: 22.08.2014
 */
@Controller
public class RootController {
    @Autowired
    private UserService service;

    @Autowired
    private UserMealService mealService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("userList", service.getAll());
        return "userList";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String setUser(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        AuthorizedUser.setId(userId);
        return "redirect:meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET, params = "action=delete")
    public String mealDelete(@RequestParam("action") String action, @RequestParam("id") int id, Model model) {
        mealService.delete(id, AuthorizedUser.id);
        model.addAttribute("mealList", UserMealsUtil.getWithExceeded(mealService.getAll(AuthorizedUser.id), AuthorizedUser.getCaloriesPerDay()));
        return "mealList";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET, params = "action=create")
    public String mealCreate(Model model) {
        final UserMeal meal = new UserMeal(LocalDateTime.now(), "", 1000);
        model.addAttribute("meal", meal);
        return "mealEdit";
    }
    @RequestMapping(value = "/meals", method = RequestMethod.GET, params = "action=update")
    public String mealUpdate(@RequestParam("id") int id, Model model) {
        model.addAttribute("meal", mealService.get(id, AuthorizedUser.id));
        return "mealEdit";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST, params = "action=filter")
    public String mealFilter(HttpServletRequest request) {
        LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
        request.setAttribute("mealList",
                UserMealsUtil.getFilteredWithExceeded(
                        mealService.getBetweenDates(
                                startDate != null ? startDate : TimeUtil.MIN_DATE, endDate != null ? endDate : TimeUtil.MAX_DATE, AuthorizedUser.id),
                        startTime != null ? startTime : LocalTime.MIN, endTime != null ? endTime : LocalTime.MAX, AuthorizedUser.getCaloriesPerDay())
                );
        return "redirect:meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public String mealSave(HttpServletRequest request) {
        final UserMeal userMeal = new UserMeal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        if (!request.getParameter("id").isEmpty()) {
            userMeal.setId(Integer.parseInt(request.getParameter("id")));
        }
        mealService.save(userMeal, AuthorizedUser.id);
        return "redirect:meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String mealList(Model model) {
        model.addAttribute("mealList", UserMealsUtil.getWithExceeded(mealService.getAll(AuthorizedUser.id), AuthorizedUser.getCaloriesPerDay()));
        return "mealList";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
