package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
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
        log.debug("{} to {} with params{}", request.getMethod(), request.getRequestURL(), getAllRequestParams(request));
        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id.equals("")) {
            mealStorage.create(new Meal(mealStorage.createId(), dateTime, description, calories));
        } else if (mealStorage.get(Integer.parseInt(id)) != null) {
            mealStorage.update(new Meal(Integer.parseInt(id), dateTime, description, calories));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("{} to {} with params{}", request.getMethod(), request.getRequestURL(), getAllRequestParams(request));
        String action = request.getParameter("action");
        if (action == null) {
            forwardToMeals(request, response);
            return;
        }
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