package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    private static final String CSS_URL = "/resources/css/style.css";

    @Test
    void getCss() throws Exception {
        perform(MockMvcRequestBuilders.get(CSS_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("text/css;charset=UTF-8"));
    }
}