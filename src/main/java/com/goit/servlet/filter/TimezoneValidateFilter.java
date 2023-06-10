package com.goit.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

//@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {

    private Set<String> timeZones = new HashSet<>();

    @Override
    public void init() {
            timeZones = Set.of(TimeZone.getAvailableIDs());
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timezone");
        String timeZoneShort = "";
        if (timeZone != null) {
            if (timeZone.contains("-")) {
                timeZoneShort = timeZone.split("-")[0];
            } else if (timeZone.contains(" ")) {
                timeZoneShort = timeZone.split(" ")[0];
            }
            if (!timeZones.contains(timeZoneShort) || timeZone.length() == timeZoneShort.length()) {
                sendWrongTimeZone(res);
            }
        }
        chain.doFilter(req, res);
    }

    private static void sendWrongTimeZone(HttpServletResponse res) throws IOException {
        final int invalidTimeZoneCode = 400;
        res.setStatus(invalidTimeZoneCode);
        res.setContentType("text/html; charset=utf-8");
        res.getWriter().write("invalid timezone");
        res.getWriter().close();
    }
}
