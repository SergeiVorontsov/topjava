package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Config;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.RandomUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(ru.javawebinar.topjava.web.MealServlet.class);
    private Storage storage;
    DateTimeFormatter formatter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
        formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal m = storage.get(id);
        if (m == null) {
            storage.save(new Meal(RandomUtil.createId(), dateTime, description, calories));
        } else {
            m.setDateTime(dateTime);
            m.setDescription(description);
            m.setCalories(calories);
            storage.update(m);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> meals = MealsUtil.listWithoutFilter(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal m;
        String id = request.getParameter("id");
        switch (action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                m = storage.get(Integer.parseInt(id));
                break;
            case "create":
                m = MealsUtil.EMPTY;
                break;
            default:
                throw new IllegalStateException("Action " + action + "is illegal");
        }
        request.setAttribute("meal", m);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}