package com.example.socialdrinks.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class SimpleServletServer {

    public static class SimpleServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("> " + req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol());
            Enumeration<String> headerNames = req.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                Enumeration<String> values = req.getHeaders(name);
                while (values.hasMoreElements()) {
                    System.out.println("> " + name + ": " + values.nextElement());
                }
            }
            System.out.println(">");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<html><body><h1>It works!</h1></body></html>");
            }
        }

    }

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String baseDir = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", baseDir);

        String servletName = "simpleServlet";
        Tomcat.addServlet(context, servletName, new SimpleServlet());
        context.addServletMappingDecoded("/", servletName);

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

}
