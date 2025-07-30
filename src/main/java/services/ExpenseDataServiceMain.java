package services;

import org.glassfish.jersey.logging.LoggingFeature;
import security.ExpenseCategoryException;
import security.MyDynamicFeature;
import security.TestFilter;
import managers.BookManager;
import managers.CategoryManager;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;


import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseDataServiceMain {

    private final static java.util.logging.Logger LOGGER =
            Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME);

    public static void main(String[] args ){
        LOGGER.setLevel(Level.ALL);
        LOGGER.info("start my server");
        // scan packages
        final ResourceConfig config = new ResourceConfig();
        //config.register(TestFilter.class);
        config.register(MyDynamicFeature.class);

        //config.register(new LoggingFeature(LOGGER, LoggingFeature.Verbosity.PAYLOAD_ANY));
        //config.register(LoggingFeature.class);

        //final ResourceConfig config = new ResourceConfig().packages("services");
        // https://stackoverflow.com/questions/30215676/jersey-is-there-a-way-to-instantiate-a-per-request-resource-with-parameters
        CategoryManager categoryManager = new CategoryManager();
        BookManager bookManager = new BookManager(categoryManager);
        config.register(new CategoryService(categoryManager));
        config.register(new Expenses(bookManager));
        config.register(BroadcastingService.class);
        config.register(ExpenseCategoryException.class);
        ConsoleHandler handler = new ConsoleHandler();
        LOGGER.addHandler(handler);

        // Start Jetty Server
        final Server server =
                JettyHttpContainerFactory.createServer(
                        URI.create("http://localhost:8080/"), config);
    }

}
