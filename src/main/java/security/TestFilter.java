package security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
public class TestFilter implements ContainerRequestFilter {

    static Logger logger = LoggerFactory.getLogger(TestFilter.class);
    public static final String AUTHENTICATION_HEADER = "Authorization";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("TestFilter called");

        String authCredentials = containerRequestContext.getHeaderString(AUTHENTICATION_HEADER);

        // better injected
        AuthenticationService authenticationService = new AuthenticationService();

        boolean authenticationStatus = authenticationService.authenticate(authCredentials);

        if (!authenticationStatus) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User cannot access the resource.").header("WWW-Authenticate", "Basic").build());
        }

    }
}
