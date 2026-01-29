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

public class ThymeleafServletServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String baseDir = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", baseDir);

        Tomcat.addServlet(context, "helloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello", "helloServlet");

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    public static class HelloServlet extends HttpServlet {

        private static final TemplateEngine TEMPLATE_ENGINE = createTemplateEngine();

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");

            IContext thymeleafContext = new org.thymeleaf.context.Context();

            String viewName = "hello";

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
}
