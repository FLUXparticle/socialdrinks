package com.example.socialdrinks.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.catalina.*;
import org.apache.catalina.startup.*;
import org.apache.tomcat.util.descriptor.web.*;
import java.io.*;

public class FilteredServletServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String baseDir = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", baseDir);

        Tomcat.addServlet(context, "protectedServlet", new ProtectedServlet());
        context.addServletMappingDecoded("/", "protectedServlet");

        Tomcat.addServlet(context, "loginServlet", new LoginServlet());
        context.addServletMappingDecoded("/login", "loginServlet");

        FilterDef loginFilterDef = new FilterDef();
        loginFilterDef.setFilterName("loginFilter");
        loginFilterDef.setFilter(new LoginFilter());
        context.addFilterDef(loginFilterDef);

        FilterMap loginFilterMap = new FilterMap();
        loginFilterMap.setFilterName("loginFilter");
        loginFilterMap.addURLPattern("/");
        context.addFilterMap(loginFilterMap);

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    public static class ProtectedServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<html><body><h1>It works!</h1></body></html>");
            }
        }
    }

    public static class LoginServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<html><body>");
                out.println("<h1>Login</h1>");
                out.println("<form method='post' action='/login'>");
                out.println("<button type='submit'>Login</button>");
                out.println("</form>");
                out.println("</body></html>");
            }
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            req.getSession(true).setAttribute("loggedIn", Boolean.TRUE);
            resp.sendRedirect("/");
        }
    }

    public static class LoginFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            String path = req.getRequestURI();
            if (path.startsWith("/login")) {
                chain.doFilter(request, response);
                return;
            }

            HttpSession session = req.getSession(false);
            Object loggedIn = (session == null) ? false : session.getAttribute("loggedIn");
            if (Boolean.TRUE.equals(loggedIn)) {
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect("/login");
            }
        }

        @Override
        public void destroy() {
        }
    }
}
