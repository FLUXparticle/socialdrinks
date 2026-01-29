package com.example.socialdrinks.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.*;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DispatcherServletServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String baseDir = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", baseDir);

        Tomcat.addServlet(context, "dispatcherServlet", new DispatcherServlet(new HelloController()));
        context.addServletMappingDecoded("/*", "dispatcherServlet");

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    public static class DispatcherServlet extends HttpServlet {
        private static final TemplateEngine TEMPLATE_ENGINE = createTemplateEngine();
        private final HelloController controller;

        public DispatcherServlet(HelloController controller) {
            this.controller = controller;
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String path = req.getRequestURI();
            Map<String, Object> model = new HashMap<>();

            String viewName = switch (path) {
                case "/hello" -> controller.getHello(model);
                default -> null;
            };

            render(resp, viewName, model);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String path = req.getRequestURI();
            Map<String, Object> model = new HashMap<>();

            String viewName = switch (path) {
                case "/hello" -> controller.postHello(req.getParameter("name"), model);
                default -> null;
            };

            render(resp, viewName, model);
        }

        private void render(HttpServletResponse resp, String viewName, Map<String, Object> model) throws IOException {
            if (viewName == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("text/plain");
                resp.getWriter().println("404 Not Found");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");

            IContext thymeleafContext = new org.thymeleaf.context.Context(Locale.getDefault(), model);

            TEMPLATE_ENGINE.process(viewName, thymeleafContext, resp.getWriter());
        }

        private static TemplateEngine createTemplateEngine() {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML");
            resolver.setCharacterEncoding("UTF-8");

            TemplateEngine engine = new TemplateEngine();
            engine.setTemplateResolver(resolver);
            return engine;
        }
    }

    public static class HelloController {
        public String getHello(Map<String, Object> model) {
            return "hallo_name";
        }

        public String postHello(String name, Map<String, Object> model) {
            model.put("name", name);
            return "hallo_name";
        }
    }

    public enum HttpMethod {
        GET,
        POST
    }
}
