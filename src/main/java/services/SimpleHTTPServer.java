package services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class SimpleHTTPServer {
    public static void main(String[] args ) throws Exception {
        // Start Jetty Server
/*        Server server = new Server(9090);
        // This has a connector listening on port 9090
        // and no handlers, meaning all requests will result
        // in a 404 response
        server.start();
        System.err.println("Hint: Hit Ctrl+C to stop Jetty.");
        server.join();*/

        Server server = new Server();

/*        HttpConfiguration httpConfigOff = new HttpConfiguration();
        httpConfigOff.setSendDateHeader(false);
        httpConfigOff.setSendServerVersion(false);
        httpConfigOff.setSendXPoweredBy(false);

        ServerConnector connectorOff = new ServerConnector(server, new HttpConnectionFactory(httpConfigOff));
        connectorOff.setPort(9090);
        server.addConnector(connectorOff);*/

        HttpConfiguration httpConfigDefault = new HttpConfiguration();

        ServerConnector connectorDefault = new ServerConnector(server, new HttpConnectionFactory(httpConfigDefault));
        connectorDefault.setPort(9191);
        server.addConnector(connectorDefault);

        server.setHandler(new AbstractHandler()
        {

            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

                if (request.getPathInContext().equals("/aaa")) {
                httpServletResponse.setContentType("text/html; charset=utf-8");
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);

                PrintWriter out = httpServletResponse.getWriter();

                out.println("<h1>" + "greeting aaa" + "</h1>");


                request.setHandled(true);
                }
                else  if (request.getPathInContext().equals("/bbb")) {
                    httpServletResponse.setContentType("text/html; charset=utf-8");
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);

                    PrintWriter out = httpServletResponse.getWriter();

                    out.println("<h1>" + "greeting bbb" + "</h1>");

                    request.setHandled(true);
                }
/*
/*
                httpServletResponse.addHeader(HttpHeader.CONTENT_TYPE.name(), "text/plain; charset=utf-8");
                httpServletResponse.getWriter().write(BufferUtil.toBuffer("Greetings.", StandardCharsets.UTF_8).toString());
*/
            }
        });

        server.start();

        server.join();

    }

    //https://portail.capsana.ca/doc/9.4.5.v20170502/embedding-jetty.html
    //https://www.baeldung.com/jetty-embedded

}
