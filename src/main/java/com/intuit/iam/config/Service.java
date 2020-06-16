package com.intuit.iam.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Service {
    private static final Logger logger = LogManager.getLogger(Service.class);

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        final Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages", "com.intuit.iam");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception ex) {
            logger.error("Error occurred while starting Jetty", ex);
            System.exit(1);
        } finally {
            jettyServer.destroy();
        }
    }
}

