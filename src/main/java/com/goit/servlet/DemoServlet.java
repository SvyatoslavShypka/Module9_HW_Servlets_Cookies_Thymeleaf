package com.goit.servlet;

import com.goit.crud.entity.CustomerEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@WebServlet(value = "/api/v1/demo")
@Slf4j
public class DemoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        log.info("INIT METHOD");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = String.format("GET METHOD:%s", req.getRequestURI());
        log.info(message);
        CustomerEntity customer = CustomerEntity.of(1L, "Name", "contact", "Ukraine");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(gson.toJson(customer));
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = String.format("POST METHOD:%s", req.getRequestURI());
        log.info(message);

        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes);
        log.debug("accepted request:{}", body);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(body);
        resp.getWriter().close();
    }

    @Override
    public void destroy() {
        log.info("DESTROY METHOD");
    }
}
