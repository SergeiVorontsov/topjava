package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Controller
@RequestMapping("/meals")
public class JspMealController extends RootController {
    @Autowired
    private MealService service;

    @GetMapping()
    public String getMeals(Model model) {
        log.info("meals");
        model.addAttribute(
                "meals",
                MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay())
        );
        return "meals";
    }

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        log.info("delete meal {} for user {}", mealId, userId);
        service.delete(mealId, userId);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String createMeal(Model model) {
        log.info("create new meal");
        model.addAttribute(
                "meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)
        );
        return "mealForm";
    }

    @GetMapping("/update")
    public String updateMeal(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        log.info("update meal {} for user {}", mealId, userId);
        model.addAttribute(
                "meal",
                service.get(mealId, userId)
        );
        return "mealForm";
    }

    @PostMapping("/save")
    public String saveMeal(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.hasLength(request.getParameter("id"))) {
            meal.setId(Integer.parseInt(request.getParameter("id")));
            log.info("update in DB meal {} for user {}", meal.id(), userId);
            service.update(meal, userId);
        } else {
            log.info("insert in DB meal {} for user {}", meal.getDescription(), userId);
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filterMeals(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        log.info("filter meals");
        model.addAttribute(
                "meals",
                MealsUtil.getTos(
                        service.getBetweenInclusive(startDate, endDate, userId),
                        SecurityUtil.authUserCaloriesPerDay())
        );
        return "meals";
    }
}