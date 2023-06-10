package com.goit.thymeleaf;

import com.goit.conf.LoggingConfiguration;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

//@WebServlet("/time")
public class ThymeleafTimeController extends HttpServlet {

        private TemplateEngine engine;

        @Override
        public void init(ServletConfig config) throws ServletException {
            new LoggingConfiguration().setup();
            engine = new TemplateEngine();

            JakartaServletWebApplication app = JakartaServletWebApplication.buildApplication(config.getServletContext());

            WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);

            resolver.setPrefix("/WEB-INF/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setOrder(engine.getTemplateResolvers().size());
            resolver.setCacheable(false);
            engine.addTemplateResolver(resolver);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String adjustTime = "";
            Long correctTime;
            String timeZone = req.getParameter("timezone");
            LocalDateTime currentTime = LocalDateTime.now();
            ZoneId zone = ZoneId.of("UTC");
            ZonedDateTime currentUtcTime = ZonedDateTime.of(currentTime, zone);
            Cookie[] cookies = req.getCookies();
            if (timeZone == null) {
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("lastTimezone")) {
                            timeZone = cookie.getValue();
                        }
                    }
                }
            }

            if (timeZone != null) {
                if (timeZone.contains("-")) {
                    adjustTime = "-" + timeZone.split("-")[1];
                } else if (timeZone.contains(" ")) {
                    adjustTime = "+" + timeZone.split(" ")[1];
                } else if (timeZone.contains("+")) {
                    adjustTime = "+" + timeZone.split("\\+")[1];
                }
                try {
                    correctTime = Long.parseLong(adjustTime);
                    currentUtcTime = currentUtcTime.plusHours(correctTime);
                } catch (Exception e) {
                    adjustTime = "";
                }
                Cookie cookie = new Cookie("lastTimezone", "UTC" + adjustTime);
                cookie.setHttpOnly(true);
                resp.addCookie(cookie);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz");

            resp.setContentType("text/html; charset=utf-8");

            Map<String, String[]> parameterMap = req.getParameterMap();
            Map<String, Object> params = new LinkedHashMap<>();

            for (Map.Entry<String, String[]> keyValue : parameterMap.entrySet()) {
                params.put(keyValue.getKey(), currentUtcTime.format(formatter) + adjustTime);
            }
            if (parameterMap.isEmpty()) {
                params.put("timezone", currentUtcTime.format(formatter) + adjustTime);
            }

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("queryParams", params)
            );

            engine.process("time_template", simpleContext, resp.getWriter());
            resp.getWriter().close();
    }
}
