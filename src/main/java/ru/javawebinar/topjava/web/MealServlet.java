package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealMemoryStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealStorage mealStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealStorage = new MealMemoryStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        if (log.isDebugEnabled()) {
            log.debug("{} to {} with params{}", request.getMethod(), request.getRequestURL(), getAllRequestParams(request));
        }
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        int id = parseId(request.getParameter("id"));
        if (id == -1) {
            mealStorage.create(dateTime, description, calories);
        } else {
            mealStorage.update(id, dateTime, description, calories);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("{} to {} with params{}", request.getMethod(), request.getRequestURL(), getAllRequestParams(request));
        }
        String action = Optional.ofNullable(request.getParameter("action")).orElse("no-action");
        switch (action) {
            case "delete":
                mealStorage.delete(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("meals");
                log.debug("redirect to meals.jsp");
                return;
            case "edit":
                request.setAttribute("meal", mealStorage.get(Integer.parseInt(request.getParameter("id"))));
                break;
            case "create":
                request.setAttribute("meal", MealsUtil.empty);
                break;
            default:
                forwardToMeals(request, response);
                return;
        }
        request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
        log.debug("forward to editMeal.jsp");
    }

    private int parseId(String id) {
        return !id.isEmpty() ? Integer.parseInt(id) : -1;
    }

    private String getAllRequestParams(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .map(s -> s.getKey() + Arrays.toString(s.getValue())).reduce(": ", String::concat);
    }

    private void forwardToMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> meals = MealsUtil.listWithoutFilter(mealStorage.getAll(), MealsUtil.CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        log.debug("forward to meals.jsp");
    }
}