package services;

import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyHTTPServletServer {

    public static final String BASE_URL = "http://localhost:8080/";

    public static Server startServer2() throws Exception {
        Server server = new Server(7070);
        ServletContextHandler handler = new ServletContextHandler(server, "/servlets");
        handler.addServlet(ExampleServlet.class, "/example");

        server.start();
        return server;

    }

    public static void main(String[] args){

        try {
            final Server server = startServer2();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.stop();
                    System.out.println("Done, exit.");
                }catch (Exception e){
                    Logger.getLogger(MyHTTPServletServer.class.getName()).log(Level.SEVERE, null, e);
                }
            }));

            System.out.println(String.format("Application started.%nStop the application using CTRL+C"));

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
