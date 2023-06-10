package com.goit.thymeleaf;

import com.goit.crud.datasource.Datasource;
import com.goit.crud.entity.CustomerEntity;
import com.goit.crud.repository.CustomerRepositoryIml;
import com.goit.crud.repository.JDBCRepository;
import com.goit.thymeleaf.exception.ThymeleafAppInternalException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.goit.util.Constants.BASE_URL;
import static com.goit.util.Constants.THYMELEAF;

@Slf4j
//@WebServlet(value = "/customers/*", loadOnStartup = 1)
public class CustomerServlet extends HttpServlet {

    private String baseUrl;
    private TemplateEngine templateEngine;
    private JDBCRepository<CustomerEntity> customerEntityJDBCRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("INIT CustomerServlet");
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute(THYMELEAF);
        baseUrl = (String) config.getServletContext().getAttribute(BASE_URL);
        customerEntityJDBCRepository = new CustomerRepositoryIml(new Datasource());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createCustomerPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("POST");
        String customerName = req.getParameter("customerName");
        String contactName = req.getParameter("contactName");
        String country = req.getParameter("country");
        CustomerEntity entity = CustomerEntity.of(null, customerName, contactName, country);
        customerEntityJDBCRepository.save(entity);
        createCustomerPage(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] parts = req.getRequestURI().split("/");
        String id = parts[parts.length - 1];
        customerEntityJDBCRepository.delete(Long.parseLong(id));
    }

    private void createCustomerPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html; charset=utf-8");
            List<CustomerEntity> customers = customerEntityJDBCRepository.findAll();

            Map<String, Object> pageParameters = Map.of(
                    "customers", customers,
                    "baseUrl", baseUrl
            );

            Context simpleContext = new Context(
                    req.getLocale(),
                    pageParameters
            );

            templateEngine.process("customers", simpleContext, resp.getWriter());
            resp.getWriter().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ThymeleafAppInternalException();
        }
    }

    private void createTimePage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html; charset=utf-8");
            List<CustomerEntity> customers = customerEntityJDBCRepository.findAll();

            Map<String, Object> pageParameters = Map.of(
                    "customers", customers,
                    "baseUrl", baseUrl
            );

            Context simpleContext = new Context(
                    req.getLocale(),
                    pageParameters
            );

            templateEngine.process("customers", simpleContext, resp.getWriter());
            resp.getWriter().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ThymeleafAppInternalException();
        }
    }
}
